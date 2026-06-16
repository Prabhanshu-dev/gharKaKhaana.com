package org.gharKaKhaana.auth.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ApiResponse<T> — Standard response envelope for ALL auth-service API responses.
 *
 * Every endpoint returns this wrapper, ensuring a consistent contract:
 * {
 *   "success": true,
 *   "message": "Login successful.",
 *   "data": { ... }
 * }
 *
 * On error, GlobalExceptionHandler produces:
 * {
 *   "success": false,
 *   "message": "An account with this email already exists.",
 *   "data": null
 * }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }
}
