package com.dimihris.accountsservice.service;

import com.dimihris.accountsservice.dto.CustomerDetailsDto;

public interface CustomerService {

    CustomerDetailsDto findCustomerDetails(String mobileNumber);
}
