package com.project.ecommerce.service;

import com.project.ecommerce.dto.CategoryRequest;
import com.project.ecommerce.dto.CategoryResponse;
import com.project.ecommerce.model.Category;

import java.util.List;

public interface CategoryService {
    default CategoryResponse getCategories(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder) {
        return null;
    }

    default CategoryRequest addCategory(CategoryRequest categoryRequest) {
return  null;
    }
    default CategoryRequest deleteCategory(Long categoryId){
        return null;

    }
    default CategoryRequest updateCategory(CategoryRequest categoryRequest,Long categoryId){
        return null;
    }
}
