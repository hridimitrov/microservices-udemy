package com.dimihris.accountsservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Schema(
        name = "Customer",
        description = "Customer details used to create and manage an account"
)
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
public class CustomerDto {

    @Schema(
            description = "Customer's full name",
            example = "John Doe"
    )
    @Size(min = 2, max = 30)
    @NotEmpty(message = "Name cannot be null or empty")
    private String name;

    @Schema(
            description = "Customer's email address",
            example = "john@example.com"
    )
    @Email(message = "Email address should be a valid value")
    @NotEmpty(message = "Email address cannot be null or empty")
    private String email;

    @Schema(
            description = "Customer's 10-digit mobile number",
            example = "0888123456"
    )
    @Pattern(
            regexp = "(^$|[0-9]{10})",
            message = "Mobile number must be 10 digits"
    )
    private String mobileNumber;
}
