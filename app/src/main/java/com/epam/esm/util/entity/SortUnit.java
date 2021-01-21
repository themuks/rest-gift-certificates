package com.epam.esm.util.entity;

import lombok.Data;

/**
 * Sort unit is entity for {@link com.epam.esm.util.QueryCustomizer} which represents field name (see {@link #fieldName}) and
 * the way this field will be sorted (see {@link #sortType}) during query to data source.
 */
@Data
public class SortUnit {
    private final String fieldName;
    private final String sortType;
}
