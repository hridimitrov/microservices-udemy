package com.dimihris.accountsservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(
        name = "Response",
        description = "Standard API response containing the operation status"
)
@Data
@AllArgsConstructor
public class ResponseDto {

    @Schema(
            description = "HTTP status code returned by the operation",
            example = "200"
    )
    private String statusCode;

    @Schema(
            description = "Human-readable message describing the result of the operation",
            example = "Request processed successfully"
    )
    private String statusMessage;
}
