package com.epam.esm.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Thrown to indicate that request to controller has errors
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private String errorMessage;
    private String errorCode;
}
