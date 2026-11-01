package com.dimihris.accountsservice.service.client;

import com.dimihris.accountsservice.dto.CardDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("cards-service")
public interface CardsFeignClient {

    @GetMapping(value = "/api/v1/find", consumes = "application/json")
    ResponseEntity<CardDto> fetchCardDetails(
            @RequestHeader("eazybank-correlation-id") String correlationId,
            @RequestParam String mobileNumber);

}
