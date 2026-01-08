package com.project.ecommerce.controller;
import com.project.ecommerce.config.AppConstants;
import com.project.ecommerce.dto.CategoryRequest;
import com.project.ecommerce.dto.CategoryResponse;
import com.project.ecommerce.model.Category;
import com.project.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
   @Autowired
    private CategoryService serviceController;


    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getCategories(
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer  pageNumber,
            @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_BY,required=false)String sortBy,
            @RequestParam(name="sortOrder",defaultValue=AppConstants.SORT_ORDER,required=false) String sortOrder)


    {
        CategoryResponse categories= serviceController.getCategories(pageNumber, pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(categories,HttpStatus.OK);
    }
    @PostMapping("/public/categories")
    public ResponseEntity<CategoryRequest> postCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
      CategoryRequest savedCategory= serviceController.addCategory(categoryRequest);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryRequest> deleteCategory(@PathVariable Long categoryId){
            CategoryRequest status = serviceController.deleteCategory(categoryId);
            return ResponseEntity.ok().body(status);

    }
    @PutMapping("/public/categories/{categoryId}")
    public  ResponseEntity<CategoryRequest> updateCategory(@Valid
                                                               @RequestBody CategoryRequest categoryRequest,
                                                           @PathVariable Long categoryId) {

        CategoryRequest savedStatus = serviceController.updateCategory(categoryRequest, categoryId);
            return new ResponseEntity<>(savedStatus, HttpStatus.OK);

    }
}
