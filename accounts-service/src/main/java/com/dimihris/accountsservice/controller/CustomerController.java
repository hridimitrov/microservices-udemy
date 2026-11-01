package com.dimihris.accountsservice.controller;

import com.dimihris.accountsservice.dto.CustomerDetailsDto;
import com.dimihris.accountsservice.service.CustomerService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(path = "/api/v1/customer", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @GetMapping("/find")
    public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(
            @RequestHeader("eazybank-correlation-id")
            String correlationId,
            @RequestParam
            @Pattern(
                    regexp = "[0-9]{10}",
                    message = "Mobile number must be 10 digits"
            )
            String mobileNumber) {
        logger.debug("banking-app-corelation-id found: {}", correlationId);
        CustomerDetailsDto customerDetailsDto = customerService.findCustomerDetails(mobileNumber, correlationId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerDetailsDto);
    }
}
