package com.epam.esm.model.validator;

public class QueryParameterValidator {
    private static final int ZERO = 0;

    private QueryParameterValidator() {
    }

    public static boolean isOffsetValid(int offset) {
        return offset >= ZERO;
    }

    public static boolean isLimitValid(int limit) {
        return limit >= ZERO;
    }
}
