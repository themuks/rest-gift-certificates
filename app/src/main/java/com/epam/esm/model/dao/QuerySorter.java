package com.epam.esm.model.dao;

import lombok.ToString;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Component
@ToString
public class QuerySorter {
    private static final String DELIMITER = ", ";
    private static final String PREFIX = "ORDER BY ";
    private static final String SUFFIX = ";";
    private static final String FORMAT_STRING = "%s %s";
    private static final String SORT_FIELD = "sortField";
    private static final String SORT_TYPE = "sortType";
    private static final String ASC = "ASC";
    private List<SortUnit> sortUnitList;

    public QuerySorter() {
    }

    public QuerySorter(List<SortUnit> sortUnitList) {
        this.sortUnitList = sortUnitList;
    }

    public QuerySorter(MultiValueMap<String, String> sortMap) {
        if (sortMap == null) {
            return;
        }
        List<String> sortFields = sortMap.get(SORT_FIELD);
        List<String> sortTypes = sortMap.get(SORT_TYPE);
        if (sortFields == null || sortTypes == null) {
            return;
        }
        List<SortUnit> sortUnits = new ArrayList<>();
        int size = sortFields.size();
        for (int i = 0; i < size; i++) {
            SortUnit sortUnit;
            if (i < sortTypes.size()) {
                sortUnit = new SortUnit(sortFields.get(i), sortTypes.get(i));
            } else {
                sortUnit = new SortUnit(sortFields.get(i), ASC);
            }
            sortUnits.add(sortUnit);
        }
        this.sortUnitList = sortUnits;
    }

    public String prepareQuery(String sql) {
        if (sortUnitList == null) {
            return sql;
        }
        StringJoiner stringJoiner = new StringJoiner(DELIMITER, PREFIX, SUFFIX);
        for (SortUnit sortUnit : sortUnitList) {
            stringJoiner.add(String.format(FORMAT_STRING, sortUnit.getFieldName(), sortUnit.getSortType().toUpperCase()));
        }
        System.out.println(String.format(FORMAT_STRING, sql, stringJoiner.toString()));
        return String.format(FORMAT_STRING, sql, stringJoiner.toString());
    }
}
