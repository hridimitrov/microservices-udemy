package com.dimihris.accountsservice.service.impl;

import com.dimihris.accountsservice.constatns.AccountsConstants;
import com.dimihris.accountsservice.dto.AccountsMsgDto;
import com.dimihris.accountsservice.dto.CustomerAccountsDto;
import com.dimihris.accountsservice.dto.CustomerDto;
import com.dimihris.accountsservice.entity.Accounts;
import com.dimihris.accountsservice.entity.Customer;
import com.dimihris.accountsservice.exception.CustomerAlreadyExistsException;
import com.dimihris.accountsservice.exception.ResourceNotFoundException;
import com.dimihris.accountsservice.repository.AccountsRepository;
import com.dimihris.accountsservice.repository.CustomerRepository;
import com.dimihris.accountsservice.service.AccountsService;
import com.dimihris.accountsservice.util.mapper.CustomerAccountsMapper;
import com.dimihris.accountsservice.util.mapper.CustomerMapper;
import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.Random;

@AllArgsConstructor
@Service
public class AccountsServiceImpl implements AccountsService {
    @Override
    public boolean updateCommunicationStatus(Long accountNumber) {
        boolean isUpdated = false;

        if (accountNumber != null) {
            Accounts accounts = accountsRepository.findById(accountNumber).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountNumber.toString())
            );
            accounts.setCommunicationSwitch(true);
            accountsRepository.save(accounts);
            isUpdated = true;
        }
        return isUpdated;
    }

    private CustomerRepository customerRepository;
    private AccountsRepository accountsRepository;
    private StreamBridge streamBridge;
    private static final Logger logger = LoggerFactory.getLogger(AccountsServiceImpl.class);
    private static final Random RANDOM = new Random();

    @Override
    public void createAccount(CustomerDto customerDto) {
        customerRepository.findByMobileNumber(customerDto.getMobileNumber())
                .ifPresent(customer -> {
                    throw new CustomerAlreadyExistsException(
                            "Customer already exists with the given mobile number"
                    );
                });

        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Customer savedCustomer = customerRepository.save(customer);

        Accounts newAccount = createNewAccount(savedCustomer);
        accountsRepository.save(newAccount);
        sendCommunication(newAccount, savedCustomer);
    }

    @Override
    public CustomerAccountsDto findAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));

        return new CustomerAccountsDto(
                customer.getName(),
                customer.getEmail(),
                customer.getMobileNumber(),
                accounts.getAccountNumber().toString(),
                accounts.getAccountType(),
                accounts.getBranchAddress()
        );
    }

    @Override
    public boolean updateCustomerAccountDetails(CustomerAccountsDto customerAccountsDto) {
        Accounts accounts = accountsRepository.findById(Long.parseLong(customerAccountsDto.getAccountNumber()))
                .orElseThrow(() -> new ResourceNotFoundException("Account", "accountNumber", customerAccountsDto.getAccountNumber()));

        CustomerAccountsMapper.mapToAccount(customerAccountsDto, accounts);
        accountsRepository.save(accounts);

        Customer customer = customerRepository.findById(accounts.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "customerId", accounts.getCustomerId().toString()));

        CustomerAccountsMapper.mapToCustomer(customerAccountsDto, customer);
        customerRepository.save(customer);

        return true;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());

        return true;
    }

    private Accounts createNewAccount(Customer customer) {
        Accounts account = new Accounts();

        account.setCustomerId(customer.getCustomerId());
        account.setAccountNumber(generateAccountNumber());
        account.setAccountType(AccountsConstants.SAVINGS);
        account.setBranchAddress(AccountsConstants.ADDRESS);

        return account;
    }

    private long generateAccountNumber() {
        return 1_000_000_000L + RANDOM.nextInt(900_000_000);
    }

    private void sendCommunication(Accounts accounts, Customer customer) {

        AccountsMsgDto accountsMsgDto = new AccountsMsgDto(accounts.getAccountNumber(), customer.getName(),
        customer.getEmail(), customer.getMobileNumber());

        logger.info("Sending communication request for the details: {}", accountsMsgDto);
        var result = streamBridge.send("sendCommunication-out-0", accountsMsgDto);
        logger.info("Communication request sent: {}", result);

    }
}
