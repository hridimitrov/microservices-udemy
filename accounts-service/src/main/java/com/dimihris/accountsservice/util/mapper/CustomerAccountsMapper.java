package com.dimihris.accountsservice.util.mapper;

import com.dimihris.accountsservice.dto.CustomerAccountsDto;
import com.dimihris.accountsservice.entity.Accounts;
import com.dimihris.accountsservice.entity.Customer;

public class CustomerAccountsMapper {

    public static Accounts mapToAccount(CustomerAccountsDto customerAccountsDto, Accounts accounts) {
        accounts.setAccountNumber(Long.parseLong(customerAccountsDto.getAccountNumber()));
        accounts.setAccountType(customerAccountsDto.getAccountType());
        accounts.setBranchAddress(customerAccountsDto.getBranchAddress());
        return accounts;
    }

    public static Customer mapToCustomer(CustomerAccountsDto customerAccountsDto, Customer customer) {
        customer.setName(customerAccountsDto.getName());
        customer.setEmail(customerAccountsDto.getEmail());
        customer.setMobileNumber(customerAccountsDto.getMobileNumber());
        return customer;
    }
}
