package com.epam.esm.controller.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Thrown to indicate that server has error during operation execution
 */
@Getter
@Setter
public class ControllerException extends RuntimeException {
    private String entityCode;

    public ControllerException(String message, String entityCode) {
        super(message);
        this.entityCode = entityCode;
    }
}
