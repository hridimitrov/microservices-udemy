package com.dimihris.loansservice.util.mapper;

import com.dimihris.loansservice.dto.LoanDto;
import com.dimihris.loansservice.entity.Loan;

public class LoanMapper {

    public static LoanDto mapToLoanDto(Loan loan, LoanDto loanDto) {
        loanDto.setMobileNumber(loan.getMobileNumber());
        loanDto.setLoanNumber(loan.getLoanNumber());
        loanDto.setLoanType(loan.getLoanType());
        loanDto.setTotalLoan(loan.getTotalLoan());
        loanDto.setPaidAmount(loan.getPaidAmount());
        loanDto.setOutstandingAmount(loan.getOutstandingAmount());

        return loanDto;
    }

    public static Loan mapToLoan(LoanDto loanDto, Loan loan) {
        loan.setMobileNumber(loanDto.getMobileNumber());
        loan.setLoanNumber(loanDto.getLoanNumber());
        loan.setLoanType(loanDto.getLoanType());
        loan.setTotalLoan(loanDto.getTotalLoan());
        loan.setPaidAmount(loanDto.getPaidAmount());
        loan.setOutstandingAmount(loanDto.getOutstandingAmount());

        return loan;
    }
}
