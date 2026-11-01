package com.dimihris.accountsservice.service.client;

import com.dimihris.accountsservice.dto.LoanDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LoansFallback implements LoansFeignClient {

    @Override
    public ResponseEntity<LoanDto> getLoanDetails(String correlationId, String mobileNumber) {
        return null; // for simplicity, you can have custom logic here
    }
}
