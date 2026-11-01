package com.dimihris.accountsservice.service.client;

import com.dimihris.accountsservice.dto.LoanDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("loans-service")
public interface LoansFeignClient {

    @GetMapping("/api/v1/loans/find")
    ResponseEntity<LoanDto> getLoanDetails(
            @RequestHeader("eazybank-correlation-id") String correlationId,
            @RequestParam String mobileNumber);

}
