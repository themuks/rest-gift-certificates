package com.epam.esm.util;

import com.epam.esm.util.entity.SearchUnit;
import com.epam.esm.util.entity.SortUnit;

import java.util.ArrayList;
import java.util.List;

public class CriteriaConstructor {
    private static final String DESC = "DESC";
    private static final String PERCENT = "%";

    private CriteriaConstructor() {
    }

    public static List<SearchUnit> convertListsToSearchCriteria(List<String> searchFields,
                                                                List<String> searchExpressions) {
        List<SearchUnit> searchCriteria = new ArrayList<>();
        if (searchFields == null || searchExpressions == null) {
            return searchCriteria;
        }
        int size = searchFields.size();
        for (int i = 0; i < size; i++) {
            if (i < searchExpressions.size()) {
                SearchUnit searchUnit = new SearchUnit(searchFields.get(i), searchExpressions.get(i));
                searchCriteria.add(searchUnit);
            }
        }
        return searchCriteria;
    }

    public static List<SortUnit> convertListsToSortCriteria(List<String> sortFields,
                                                            List<String> sortTypes) {
        List<SortUnit> sortCriteria = new ArrayList<>();
        if (sortFields == null || sortFields.isEmpty()) {
            return sortCriteria;
        }
        if (sortTypes == null) {
            sortTypes = new ArrayList<>();
        }
        int size = sortFields.size();
        for (int i = 0; i < size; i++) {
            SortUnit sortUnit = new SortUnit();
            sortUnit.setSortField(sortFields.get(i));
            if (i < sortTypes.size()) {
                if (sortTypes.get(i).equalsIgnoreCase(DESC)) {
                    sortUnit.setAscending(false);
                }
            }
            sortCriteria.add(sortUnit);
        }
        return sortCriteria;
    }
}
