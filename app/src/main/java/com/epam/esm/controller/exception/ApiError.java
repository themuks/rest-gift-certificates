package com.epam.esm.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contains response information for user when exception is thrown
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private String errorMessage;
    private String errorCode;
}
