package com.epam.esm.model.dao;

import lombok.Data;

@Data
public class SearchUnit {
    private final String fieldName;
    private final String searchExpression;
}
