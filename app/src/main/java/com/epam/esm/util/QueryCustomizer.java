package com.epam.esm.util;

import com.epam.esm.util.entity.SearchUnit;
import com.epam.esm.util.entity.SortUnit;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * The type Query customizer. Used for modifying sql queries to sort information by provided fields with different
 * methods and search by specific fields with specific way (see {@link SearchUnit} and {@link SortUnit}).
 */
@NoArgsConstructor
@Component
public class QueryCustomizer {
    private static final String COMMA_DELIMITER = ", ";
    private static final String ORDER_BY_PREFIX = "ORDER BY ";
    private static final String EMPTY_SUFFIX = "";
    private static final String ASC = "ASC";
    private static final String RESULT_FORMAT = "%s %s %s";
    private static final String EMPTY_STRING = "";
    private static final String WHERE_PREFIX = "WHERE ";
    private static final String OR_DELIMITER = " OR ";
    private static final String SEARCH_FORMAT = "%s LIKE %s";
    private static final String STRING_VALUE_FORMAT = "'%%%s%%'";
    private static final String SORT_FORMAT = "%s %s";
    private static final String AND = "AND ";
    private static final String SLASH_REGEX = "\\\\";
    private static final String TWO_SLASHES_REGEX = "\\\\\\\\";
    private static final String BACKSPACE_REGEX = "\b";
    private static final String SINGLE_QUOTE_REGEX = "'";
    private static final String DOUBLE_QUOTE_REGEX = "\"";
    private static final String ESCAPED_DOUBLE_QUOTE_REGEX = "\\\"";
    private static final String ESCAPED_SINGLE_QUOTE_REGEX = "\\'";
    private static final String ESCAPED_NUL_REGEX = "\\0";
    private static final String NUL_REGEX = "\\x00";
    private static final String ESCAPED_CONTROL_Z_REGEX = "\\Z";
    private static final String CONTROL_Z_REGEX = "\\x1A";
    private static final String NEW_LINE_REGEX = "\n";
    private static final String ESCAPED_NEW_LINE_REGEX = "\\n";
    private static final String ESCAPED_BACKSPACE_REGEX = "\\b";
    private static final String CARRIAGE_RETURN_REGEX = "\r";
    private static final String ESCAPED_CARRIAGE_RETURN_REGEX = "\\r";
    private static final String TAB_REGEX = "\t";
    private static final String ESCAPED_TAB_REGEX = "\\t";
    private List<SortUnit> sortUnits;
    private List<SearchUnit> searchUnits;

    /**
     * Instantiates a new Query customizer.
     *
     * @param sortFields        the sort fields
     * @param sortTypes         the sort types
     * @param searchFields      the search fields
     * @param searchExpressions the search expressions
     */
    public QueryCustomizer(List<String> sortFields,
                           List<String> sortTypes,
                           List<String> searchFields,
                           List<String> searchExpressions) {
        prepareSortFields(sortFields, sortTypes);
        prepareSearchFields(searchFields, searchExpressions);
    }

    /**
     * Prepare query string by adding {@code ORDER BY} and {@code LIKE} instructions, if necessary,
     * to the end of query.
     *
     * @param sql sql query
     * @return result sql query
     */
    public String prepareQuery(String sql) {
        String searchOperation = generateSearchOperation();
        String sortOperation = generateSortOperation();
        return String.format(RESULT_FORMAT, sql, searchOperation, sortOperation);
    }

    private String generateSortOperation() {
        if (sortUnits == null) {
            return EMPTY_STRING;
        }
        StringJoiner sortStringJoiner = new StringJoiner(COMMA_DELIMITER, ORDER_BY_PREFIX, EMPTY_SUFFIX);
        for (SortUnit sortUnit : sortUnits) {
            String fieldName = escapeStringForMySQL(sortUnit.getFieldName());
            String sortType = escapeStringForMySQL(sortUnit.getSortType().toUpperCase());
            String formattedString = String.format(SORT_FORMAT, fieldName, sortType);
            sortStringJoiner.add(formattedString);
        }
        return sortStringJoiner.toString();
    }

    private String generateSearchOperation() {
        if (searchUnits == null || searchUnits.isEmpty()) {
            return EMPTY_STRING;
        }
        StringJoiner searchStringJoiner = new StringJoiner(OR_DELIMITER, WHERE_PREFIX, EMPTY_SUFFIX);
        for (SearchUnit searchUnit : searchUnits) {
            String fieldName = escapeStringForMySQL(searchUnit.getFieldName());
            String searchExpression = escapeStringForMySQL(
                    String.format(STRING_VALUE_FORMAT, searchUnit.getSearchExpression()));
            String formattedString = String.format(SEARCH_FORMAT, fieldName, searchExpression);
            searchStringJoiner.add(formattedString);
        }
        return searchStringJoiner.toString();
    }

    private void prepareSortFields(List<String> sortFields, List<String> sortTypes) {
        if (sortFields == null || sortFields.isEmpty()) {
            return;
        }
        if (sortTypes == null) {
            sortTypes = new ArrayList<>();
        }
        sortUnits = new ArrayList<>();
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
    }

    private void prepareSearchFields(List<String> searchFields, List<String> searchExpressions) {
        if (searchFields == null || searchExpressions == null) {
            return;
        }
        searchUnits = new ArrayList<>();
        int size = searchFields.size();
        for (int i = 0; i < size; i++) {
            if (i < searchExpressions.size()) {
                SearchUnit searchUnit = new SearchUnit(searchFields.get(i), searchExpressions.get(i));
                searchUnits.add(searchUnit);
            }
        }
    }

    private String escapeStringForMySQL(String s) {
        return s.replaceAll(SLASH_REGEX, TWO_SLASHES_REGEX)
                .replaceAll(BACKSPACE_REGEX, ESCAPED_BACKSPACE_REGEX)
                .replaceAll(NEW_LINE_REGEX, ESCAPED_NEW_LINE_REGEX)
                .replaceAll(CARRIAGE_RETURN_REGEX, ESCAPED_CARRIAGE_RETURN_REGEX)
                .replaceAll(TAB_REGEX, ESCAPED_TAB_REGEX)
                .replaceAll(CONTROL_Z_REGEX, ESCAPED_CONTROL_Z_REGEX)
                .replaceAll(NUL_REGEX, ESCAPED_NUL_REGEX)
                .replaceAll(SINGLE_QUOTE_REGEX, ESCAPED_SINGLE_QUOTE_REGEX)
                .replaceAll(DOUBLE_QUOTE_REGEX, ESCAPED_DOUBLE_QUOTE_REGEX);
    }
}
