package com.project.ecommerce.service;

import com.project.ecommerce.dto.CategoryDTO;
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
        List<CategoryDTO> categoryDTOS =category.stream()
                .map(c->modelMapper.map(c, CategoryDTO.class))
                .toList();
        CategoryResponse categoryResponse=new CategoryResponse();
        categoryResponse.setContents(categoryDTOS);
        categoryResponse.setTotalPages(categories.getTotalPages());
        categoryResponse.setPageSize(pageSize);
        categoryResponse.setPageNumber(pageNumber);
        categoryResponse.setTotalItems(categories.getTotalPages());
        categoryResponse.setHasNext(categories.hasNext());
        return categoryResponse;
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        boolean checkCategory=categoryRepository.existsByCategoryName(categoryDTO.getCategoryName().trim());
        if(checkCategory){
            throw  new APINotFoundException("Category with the name "+ categoryDTO.getCategoryName() +" already exists!!!");
        }
        Category category=modelMapper.map(categoryDTO,Category.class);
        Category savedCategory=categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long CategoryId) {
       Category category=categoryRepository.findById(CategoryId).orElseThrow(()->
               new ResourceNotFoundException("Delete","Category",CategoryId));
       categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
    }
   @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId){
        Category existCategory=categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFoundException("Update","Category", categoryDTO.getCategoryName())
        );
       existCategory.setCategoryName(categoryDTO.getCategoryName());
       Category savedCategory= categoryRepository.save(existCategory);
       return modelMapper.map(savedCategory, CategoryDTO.class);
    }
}
