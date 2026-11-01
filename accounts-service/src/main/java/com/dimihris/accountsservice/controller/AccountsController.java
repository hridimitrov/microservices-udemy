package com.dimihris.accountsservice.controller;

import com.dimihris.accountsservice.constatns.AccountsConstants;
import com.dimihris.accountsservice.dto.AccountContactInfoDto;
import com.dimihris.accountsservice.dto.CustomerAccountsDto;
import com.dimihris.accountsservice.dto.CustomerDto;
import com.dimihris.accountsservice.dto.response.ErrorResponseDto;
import com.dimihris.accountsservice.dto.response.ResponseDto;
import com.dimihris.accountsservice.service.AccountsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Accounts",
        description = "REST APIs for creating, retrieving, updating, and deleting customer accounts"
)
@Validated
@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AccountsController {

    private final AccountsService accountsService;

    private final Environment environment;

    private final AccountContactInfoDto accountContactInfoDto;

    @Value("${build.version}")
    private String buildVersion;

    @Operation(
            summary = "Create account",
            description = "Creates a new customer together with an associated account."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Customer and account created successfully",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or customer already exists",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(
            @Valid @RequestBody CustomerDto customerDto) {

        accountsService.createAccount(customerDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(
                        AccountsConstants.STATUS_201,
                        AccountsConstants.MESSAGE_201
                ));
    }

    @Operation(
            summary = "Find account",
            description = "Retrieves customer and account details using the customer's mobile number."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer account details retrieved successfully",
                    content = @Content(
                            schema = @Schema(implementation = CustomerAccountsDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid mobile number",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer account not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/find")
    public ResponseEntity<CustomerAccountsDto> findAccount(
            @Parameter(
                    description = "Customer's 10-digit mobile number",
                    example = "0888123456"
            )
            @RequestParam
            @Pattern(
                    regexp = "[0-9]{10}",
                    message = "Mobile number must be 10 digits"
            )
            String mobileNumber) {

        CustomerAccountsDto customerAccountsDto =
                accountsService.findAccount(mobileNumber);

        return ResponseEntity.ok(customerAccountsDto);
    }

    @Operation(
            summary = "Update account",
            description = "Updates the customer and account details for an existing customer."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer account details updated successfully",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer account not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Unable to update customer account details",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDto.class)
                    )
            )
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateCustomerAccountDetails(
            @Valid @RequestBody CustomerAccountsDto customerAccountsDto) {

        boolean isUpdated =
                accountsService.updateCustomerAccountDetails(customerAccountsDto);

        if (isUpdated) {
            return ResponseEntity.ok(
                    new ResponseDto(
                            AccountsConstants.STATUS_200,
                            AccountsConstants.MESSAGE_200
                    )
            );
        }

        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseDto(
                        AccountsConstants.STATUS_417,
                        AccountsConstants.MESSAGE_417_UPDATE
                ));
    }

    @Operation(
            summary = "Delete account",
            description = "Deletes the customer and associated account identified by the mobile number."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer account deleted successfully",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid mobile number",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer account not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Unable to delete customer account",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDto.class)
                    )
            )
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccountDetails(
            @Parameter(
                    description = "Customer's 10-digit mobile number",
                    example = "0888123456"
            )
            @RequestParam
            @Pattern(
                    regexp = "[0-9]{10}",
                    message = "Mobile number must be 10 digits"
            )
            String mobileNumber) {

        boolean isDeleted = accountsService.deleteAccount(mobileNumber);

        if (isDeleted) {
            return ResponseEntity.ok(
                    new ResponseDto(
                            AccountsConstants.STATUS_200,
                            AccountsConstants.MESSAGE_200
                    )
            );
        }

        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseDto(
                        AccountsConstants.STATUS_417,
                        AccountsConstants.MESSAGE_417_DELETE
                ));
    }

    @Operation(
            summary = "Get Build Information",
            description = "Get Build information that is deployed into accounts microservices"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    }

    @Operation(
            summary = "Get Java Version",
            description = "Get Java version details that is installed into accounts-service"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(environment.getProperty("JAVA_HOME"));
    }

    @GetMapping("/contact-info")
    public ResponseEntity<AccountContactInfoDto> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountContactInfoDto);
    }
}
