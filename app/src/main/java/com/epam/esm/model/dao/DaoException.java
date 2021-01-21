package com.epam.esm.model.dao;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown to indicate that data object layer has errors
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class DaoException extends Exception {
    public DaoException() {
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }
}
