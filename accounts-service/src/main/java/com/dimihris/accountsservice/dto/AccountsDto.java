package com.dimihris.accountsservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Schema(
        name = "Accounts",
        description = "Account details associated with a customer"
)
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
public class AccountsDto {

    @Schema(
            description = "Unique 10-digit account number",
            example = "1234567890"
    )
    @Pattern(
            regexp = "(^$|[0-9]{10})",
            message = "AccountNumber must be 10 digits"
    )
    @NotEmpty(message = "AccountNumber cannot be a null or empty")
    private Long accountNumber;

    @Schema(
            description = "Type of the account",
            example = "Savings"
    )
    @NotEmpty(message = "AccountType cannot be a null or empty")
    private String accountType;

    @Schema(
            description = "Address of the branch associated with the account",
            example = "123 Main Street, Sofia"
    )
    @NotEmpty(message = "BranchAddress cannot be a null or empty")
    private String branchAddress;
}
