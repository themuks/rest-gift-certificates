package com.epam.esm.model.service.validator;

public class EntityValidator {
    private static final int ZERO = 0;

    public static boolean isIdValid(long id) {
        return ZERO < id;
    }
}
