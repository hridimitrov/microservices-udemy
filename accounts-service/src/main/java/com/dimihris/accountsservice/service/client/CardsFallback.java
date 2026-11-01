package com.dimihris.accountsservice.service.client;

import com.dimihris.accountsservice.dto.CardDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CardsFallback implements CardsFeignClient{

    @Override
    public ResponseEntity<CardDto> fetchCardDetails(String correlationId, String mobileNumber) {
        return null; // for simplicity, you can have custom logic here
    }
}
