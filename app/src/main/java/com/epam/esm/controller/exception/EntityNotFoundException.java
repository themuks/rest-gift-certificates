package com.epam.esm.controller.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Thrown to indicate that requested entity wasn't found
 */
@Getter
@Setter
public class EntityNotFoundException extends RuntimeException {
    private long id;
    private String entityCode;

    public EntityNotFoundException(long id, String entityCode) {
        super("Requested resource not found (id = " + id + ")");
        this.id = id;
        this.entityCode = entityCode;
    }

    public EntityNotFoundException() {
        super();
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }
}
