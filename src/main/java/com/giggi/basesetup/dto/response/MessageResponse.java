package com.giggi.basesetup.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Generic DTO for API response messages.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Generic API response message")
public class MessageResponse {

    @Schema(description = "Response message", example = "Operation completed successfully")
    private String message;

    @Schema(description = "Response timestamp")
    private LocalDateTime timestamp;

    @Schema(description = "Success status", example = "true")
    private boolean success;

    // Constructor with message only
    public MessageResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.success = true;
    }

    // Constructor with message and success status
    public MessageResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
        this.timestamp = LocalDateTime.now();
    }

    // Static factory methods for common responses
    public static MessageResponse success(String message) {
        return new MessageResponse(message, true);
    }

    public static MessageResponse error(String message) {
        return new MessageResponse(message, false);
    }
}