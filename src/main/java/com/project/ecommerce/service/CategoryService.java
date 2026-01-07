package com.project.ecommerce.service;

import com.project.ecommerce.model.Category;

import java.util.List;

public interface CategoryService {
    default List<Category> getCategories() {
        return null;
    }

    default String addCategory(Category category) {
return  null;
    }
    default String deleteCategory(Long categoryId){
        return null;

    }
    default Category updateCategory(Category category,Long categoryId){
        return null;
    }
}
