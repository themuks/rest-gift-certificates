package com.epam.esm.controller.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerInternalErrorException extends RuntimeException {
    private String entityCode;

    public ServerInternalErrorException(String message, String entityCode) {
        super(message);
    }
}
