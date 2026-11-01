package com.dimihris.loansservice.service;

import com.dimihris.loansservice.dto.LoanDto;

public interface LoanService {

    void createLoan(String mobileNumber);

    LoanDto findLoanDetails(String mobileNumber);

    boolean updateLoan(LoanDto loanDto);

    boolean deleteLoan(String mobileNumber);
}
