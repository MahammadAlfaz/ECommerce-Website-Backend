package com.project.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {
    private List<CategoryRequest> contents;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPages;
    private Integer totalItems;
    private Boolean hasNext;

}
