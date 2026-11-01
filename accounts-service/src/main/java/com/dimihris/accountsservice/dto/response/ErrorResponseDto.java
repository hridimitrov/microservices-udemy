package com.dimihris.accountsservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Schema(
        name = "ErrorResponse",
        description = "Standard API error response containing details about a failed request"
)
@Data
@AllArgsConstructor
public class ErrorResponseDto {

    @Schema(
            description = "API endpoint where the error occurred",
            example = "uri=/api/v1/accounts"
    )
    private String apiPath;

    @Schema(
            description = "HTTP status associated with the error",
            example = "BAD_REQUEST"
    )
    private HttpStatus errorCode;

    @Schema(
            description = "Human-readable description of the error",
            example = "Customer already registered with the given mobile number"
    )
    private String errorMessage;

    @Schema(
            description = "Timestamp when the error occurred",
            example = "2026-09-01T15:30:45"
    )
    private LocalDateTime errorTimestamp;
}
