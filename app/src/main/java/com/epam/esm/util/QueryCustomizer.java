package com.epam.esm.util;

import com.epam.esm.util.entity.SearchUnit;
import com.epam.esm.util.entity.SortUnit;
import lombok.ToString;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Component
@ToString
public class QueryCustomizer {
    private static final String COMMA_DELIMITER = ", ";
    private static final String ORDER_BY_PREFIX = "ORDER BY ";
    private static final String EMPTY_SUFFIX = "";
    private static final String SORT_FIELD = "sortField";
    private static final String SORT_TYPE = "sortType";
    private static final String ASC = "ASC";
    private static final String RESULT_FORMAT = "%s %s %s";
    private static final String SEARCH_FIELD = "searchField";
    private static final String SEARCH_EXPRESSION = "searchExpression";
    private static final String EMPTY_STRING = "";
    private static final String WHERE_PREFIX = "WHERE ";
    private static final String OR_DELIMITER = " OR ";
    private static final String SEARCH_FORMAT = "%s LIKE %s";
    private static final String STRING_VALUE_FORMAT = "'%%%s%%'";
    private static final String SORT_FORMAT = "%s %s";
    private List<SortUnit> sortUnits;
    private List<SearchUnit> searchUnits;

    public QueryCustomizer() {
    }

    public QueryCustomizer(List<SortUnit> sortUnits, List<SearchUnit> searchUnits) {
        this.sortUnits = sortUnits;
        this.searchUnits = searchUnits;
    }

    public QueryCustomizer(MultiValueMap<String, String> sortMap) {
        if (sortMap == null) {
            return;
        }
        prepareSortFields(sortMap);
        prepareSearchFields(sortMap);
    }

    public String prepareQuery(String sql) {
        String searchOperation = generateSearchOperation();
        String sortOperation = generateSortOperation();
        String resultQuery = String.format(RESULT_FORMAT, sql, searchOperation, sortOperation);
        return resultQuery;
    }

    private String generateSortOperation() {
        if (sortUnits == null) {
            return EMPTY_STRING;
        }
        StringJoiner sortStringJoiner = new StringJoiner(COMMA_DELIMITER, ORDER_BY_PREFIX, EMPTY_SUFFIX);
        for (SortUnit sortUnit : sortUnits) {
            String fieldName = sortUnit.getFieldName();
            String sortType = sortUnit.getSortType().toUpperCase();
            String formattedString = String.format(SORT_FORMAT, fieldName, sortType);
            sortStringJoiner.add(formattedString);
        }
        return sortStringJoiner.toString();
    }

    private String generateSearchOperation() {
        if (searchUnits == null) {
            return EMPTY_STRING;
        }
        StringJoiner searchStringJoiner = new StringJoiner(OR_DELIMITER, WHERE_PREFIX, EMPTY_SUFFIX);
        for (SearchUnit searchUnit : searchUnits) {
            String fieldName = searchUnit.getFieldName();
            String searchExpression = String.format(STRING_VALUE_FORMAT, searchUnit.getSearchExpression());
            String formattedString = String.format(SEARCH_FORMAT, fieldName, searchExpression);
            searchStringJoiner.add(formattedString);
        }
        return searchStringJoiner.toString();
    }

    private void prepareSortFields(MultiValueMap<String, String> sortMap) {
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
        this.sortUnits = sortUnits;
    }

    private void prepareSearchFields(MultiValueMap<String, String> sortMap) {
        List<String> searchFields = sortMap.get(SEARCH_FIELD);
        List<String> searchExpressions = sortMap.get(SEARCH_EXPRESSION);
        if (searchFields == null || searchExpressions == null) {
            return;
        }
        List<SearchUnit> searchUnits = new ArrayList<>();
        int size = searchFields.size();
        for (int i = 0; i < size; i++) {
            if (i < searchExpressions.size()) {
                SearchUnit searchUnit = new SearchUnit(searchFields.get(i), searchExpressions.get(i));
                searchUnits.add(searchUnit);
            }
        }
        this.searchUnits = searchUnits;
    }
}
