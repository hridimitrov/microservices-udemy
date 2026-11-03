package com.dimihris.accountsservice.dto;

public record AccountsMsgDto(
        Long accountNumber,
        String name,
        String email,
        String mobileNumber) {
}
