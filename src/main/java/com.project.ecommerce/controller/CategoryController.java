package com.project.ecommerce.controller;
import com.project.ecommerce.config.AppConstants;
import com.project.ecommerce.dto.CategoryDTO;
import com.project.ecommerce.dto.CategoryResponse;
import com.project.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService serviceController;


    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getCategories(
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer  pageNumber,
            @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_CATEGORIES_BY,required=false)String sortBy,
            @RequestParam(name="sortOrder",defaultValue=AppConstants.SORT_ORDER,required=false) String sortOrder)


    {
        CategoryResponse categories= serviceController.getCategories(pageNumber, pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(categories,HttpStatus.OK);
    }
    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> postCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
      CategoryDTO savedCategory= serviceController.addCategory(categoryDTO);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId){
            CategoryDTO status = serviceController.deleteCategory(categoryId);
            return ResponseEntity.ok().body(status);

    }
    @PutMapping("/public/categories/{categoryId}")
    public  ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                                       @PathVariable Long categoryId) {

        CategoryDTO savedStatus = serviceController.updateCategory(categoryDTO, categoryId);
            return new ResponseEntity<>(savedStatus, HttpStatus.OK);

    }
}
