package com.project.ecommerce.controller;
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
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories= serviceController.getCategories();
        return new ResponseEntity<>(categories,HttpStatus.OK);
    }
    @PostMapping("/public/categories")
    public ResponseEntity<String> postCategory(@Valid @RequestBody Category category) {
       ;
        return new ResponseEntity<>( serviceController.addCategory(category), HttpStatus.CREATED);
    }
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
            String status = serviceController.deleteCategory(categoryId);
            return ResponseEntity.ok().body(status);

    }
    @PutMapping("/public/categories/{categoryId}")
    public  ResponseEntity<Category> updateCategory(@Valid @RequestBody Category category,@PathVariable Long categoryId) {

            Category savedStatus = serviceController.updateCategory(category, categoryId);
            return new ResponseEntity<>(savedStatus, HttpStatus.OK);

    }
}
