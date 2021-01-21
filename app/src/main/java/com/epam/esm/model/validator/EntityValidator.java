package com.epam.esm.model.validator;

public class EntityValidator {
    private static final int ZERO = 0;

    private EntityValidator() {
    }

    public static boolean isIdValid(long id) {
        return ZERO < id;
    }
}
