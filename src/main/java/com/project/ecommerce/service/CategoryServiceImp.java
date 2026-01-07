package com.project.ecommerce.service;

import com.project.ecommerce.exception.APINotFoundException;
import com.project.ecommerce.exception.ResourceNotFoundException;
import com.project.ecommerce.model.Category;
import com.project.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
public  class CategoryServiceImp implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;


    @Override
    public List<Category> getCategories() {
        List<Category> category=categoryRepository.findAll();
        if(category.isEmpty())
            throw  new APINotFoundException("Category is empty");
        return category;
    }

    @Override
    public String addCategory(Category category) {
        boolean checkCategory=categoryRepository.existsByCategoryName(category.getCategoryName().trim());
        if(checkCategory){
            throw  new APINotFoundException("Category already exists!!!");
        }
        categoryRepository.save(category);
        return String.format("Category added successfully!!!");
    }

    @Override
    public String deleteCategory(Long CategoryId) {
       Category category=categoryRepository.findById(CategoryId).orElseThrow(()->
               new ResourceNotFoundException("Delete","Category",CategoryId));
       categoryRepository.delete(category);
        return "categoryId "+category.getCategoryId()+" Has been removed";
    }
   @Override
    public Category updateCategory(Category category,Long categoryId){
        Category existCategory=categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFoundException("Update","Category",category.getCategoryName())
        );
       existCategory.setCategoryName(category.getCategoryName());
       return categoryRepository.save(existCategory);
    }
}
