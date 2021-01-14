package com.epam.esm.model.dao;

import org.springframework.stereotype.Component;

@Component
public class Sorter {
    private static final String ASC = "ASC";
    private static final String DESC = "DESC";
    private static final String SQL_FORMAT = "%s ORDER BY %s %s";
    private String fieldName;
    private boolean isAscending = true;

    public Sorter() {
    }

    public Sorter(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setAscending() {
        this.isAscending = true;
    }

    public void setDescending() {
        this.isAscending = false;
    }

    public String prepareQuery(String sql) {
        if (fieldName == null) {
            return sql;
        }
        return String.format(SQL_FORMAT, sql, fieldName, isAscending ? ASC : DESC);
    }
}
