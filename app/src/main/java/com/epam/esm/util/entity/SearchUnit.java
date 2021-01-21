package com.epam.esm.util.entity;

import lombok.Data;

/**
 * Search unit is entity for {@link com.epam.esm.util.QueryCustomizer} which represents field name (see {@link #fieldName}) and
 * the way this field will be searched (see {@link #searchExpression}) during query to data source.
 */
@Data
public class SearchUnit {
    private final String fieldName;
    private final String searchExpression;
}
