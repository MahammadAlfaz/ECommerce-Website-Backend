package com.project.ecommerce.service;

import com.project.ecommerce.dto.CategoryDTO;
import com.project.ecommerce.dto.CategoryResponse;

public interface CategoryService {
    default CategoryResponse getCategories(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder) {
        return null;
    }

    default CategoryDTO addCategory(CategoryDTO categoryDTO) {
return  null;
    }
    default CategoryDTO deleteCategory(Long categoryId){
        return null;

    }
    default CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId){
        return null;
    }
}
