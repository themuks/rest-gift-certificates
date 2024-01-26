package com.epam.esm.util.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SortUnit {
    private String sortField;
    private boolean isAscending = true;
}
