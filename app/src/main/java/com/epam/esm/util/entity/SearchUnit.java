package com.epam.esm.util.entity;

import lombok.Data;

@Data
public class SearchUnit {
    private final String fieldName;
    private final String searchExpression;
}
