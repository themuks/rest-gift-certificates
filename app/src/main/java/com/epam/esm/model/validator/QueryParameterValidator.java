package com.epam.esm.model.validator;

public class QueryParameterValidator {
    private static final int ZERO = 0;
    private static final int ONE_THOUSAND = 1000;

    private QueryParameterValidator() {
    }

    public static boolean isOffsetValid(int offset) {
        return offset >= ZERO;
    }

    public static boolean isLimitValid(int limit) {
        return limit >= ZERO && limit <= ONE_THOUSAND;
    }
}
