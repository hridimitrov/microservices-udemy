package com.dimihris.accountsservice.service;

import com.dimihris.accountsservice.dto.CustomerAccountsDto;
import com.dimihris.accountsservice.dto.CustomerDto;

public interface AccountsService {

    void createAccount(CustomerDto customerDto);

    CustomerAccountsDto findAccount(String mobileNumber);

    boolean updateCustomerAccountDetails(CustomerAccountsDto customerAccountsDto);

    boolean deleteAccount(String mobileNumber);
}
