package com.epam.esm.controller.exception;

import lombok.Getter;
import lombok.Setter;

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
}
