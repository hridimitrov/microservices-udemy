package com.dimihris.accountsservice.service.impl;

import com.dimihris.accountsservice.dto.AccountsDto;
import com.dimihris.accountsservice.dto.CardDto;
import com.dimihris.accountsservice.dto.CustomerDetailsDto;
import com.dimihris.accountsservice.dto.LoanDto;
import com.dimihris.accountsservice.entity.Accounts;
import com.dimihris.accountsservice.entity.Customer;
import com.dimihris.accountsservice.exception.ResourceNotFoundException;
import com.dimihris.accountsservice.repository.AccountsRepository;
import com.dimihris.accountsservice.repository.CustomerRepository;
import com.dimihris.accountsservice.service.CustomerService;
import com.dimihris.accountsservice.service.client.CardsFeignClient;
import com.dimihris.accountsservice.service.client.LoansFeignClient;
import com.dimihris.accountsservice.util.mapper.AccountsMapper;
import com.dimihris.accountsservice.util.mapper.CustomerMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    @Override
    public CustomerDetailsDto findCustomerDetails(String mobileNumber, String correlationId) {

        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoanDto> loanDtoResponseEntity = loansFeignClient.getLoanDetails(correlationId, mobileNumber);
        customerDetailsDto.setLoanDto(loanDtoResponseEntity.getBody());

        ResponseEntity<CardDto> cardDtoResponseEntity = cardsFeignClient.fetchCardDetails(correlationId, mobileNumber);
        customerDetailsDto.setCardDto(cardDtoResponseEntity.getBody());

        return customerDetailsDto;
    }
}