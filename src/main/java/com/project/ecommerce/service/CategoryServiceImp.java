package com.project.ecommerce.service;

import com.project.ecommerce.dto.CategoryRequest;
import com.project.ecommerce.dto.CategoryResponse;
import com.project.ecommerce.exception.APINotFoundException;
import com.project.ecommerce.exception.ResourceNotFoundException;
import com.project.ecommerce.model.Category;
import com.project.ecommerce.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
public  class CategoryServiceImp implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;



    @Override
    public CategoryResponse getCategories(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder) {
        Sort sort=sortOrder.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<Category> categories = categoryRepository.findAll(pageable);
        List<Category> category=categories.getContent();
        if(category.isEmpty())
            throw  new APINotFoundException("Category is empty");
        List<CategoryRequest> categoryRequests=category.stream()
                .map(c->modelMapper.map(c,CategoryRequest.class))
                .toList();
        CategoryResponse categoryResponse=new CategoryResponse();
        categoryResponse.setContents(categoryRequests);
        categoryResponse.setTotalPages(categories.getTotalPages());
        categoryResponse.setPageSize(pageSize);
        categoryResponse.setPageNumber(pageNumber);
        categoryResponse.setTotalItems(categories.getTotalPages());
        categoryResponse.setHasNext(categories.hasNext());
        return categoryResponse;
    }

    @Override
    public CategoryRequest addCategory(CategoryRequest categoryRequest) {
        boolean checkCategory=categoryRepository.existsByCategoryName(categoryRequest.getCategoryName().trim());
        if(checkCategory){
            throw  new APINotFoundException("Category with the name "+categoryRequest.getCategoryName() +" already exists!!!");
        }
        Category category=modelMapper.map(categoryRequest,Category.class);
        Category savedCategory=categoryRepository.save(category);
        return modelMapper.map(savedCategory,CategoryRequest.class);
    }

    @Override
    public CategoryRequest deleteCategory(Long CategoryId) {
       Category category=categoryRepository.findById(CategoryId).orElseThrow(()->
               new ResourceNotFoundException("Delete","Category",CategoryId));
       categoryRepository.delete(category);
        return modelMapper.map(category,CategoryRequest.class);
    }
   @Override
    public CategoryRequest updateCategory(CategoryRequest categoryRequest,Long categoryId){
        Category existCategory=categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFoundException("Update","Category",categoryRequest.getCategoryName())
        );
       existCategory.setCategoryName(categoryRequest.getCategoryName());
       Category savedCategory= categoryRepository.save(existCategory);
       return modelMapper.map(savedCategory,CategoryRequest.class);
    }
}
