package com.project.ecommerce.controller;

import com.project.ecommerce.config.AppConstants;
import com.project.ecommerce.dto.ProductDTO;
import com.project.ecommerce.dto.ProductResponse;
import com.project.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    @PostMapping("admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO,
                                                 @PathVariable Long categoryId) {

        ProductDTO request = productService.addProduct(productDTO, categoryId);
        return new ResponseEntity<>(request, HttpStatus.CREATED);
    }
    @GetMapping("public/products")
    public ResponseEntity<ProductResponse> getAllProduct(
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer  pageNumber,
            @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_PRODUCT_BY,required=false)String sortBy,
            @RequestParam(name="sortOrder",defaultValue=AppConstants.SORT_ORDER,required=false) String sortOrder)

    {
        ProductResponse response =productService.getAllProduct(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @GetMapping("public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductByCategory(@PathVariable Long categoryId,
                                                                @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer  pageNumber,
                                                                @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                                                @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_PRODUCT_BY,required=false)String sortBy,
                                                                @RequestParam(name="sortOrder",defaultValue=AppConstants.SORT_ORDER,required=false) String sortOrder)

    {
        ProductResponse response=productService.getProductByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(@PathVariable String keyword,
                                                               @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer  pageNumber,
                                                               @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                                               @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_PRODUCT_BY,required=false)String sortBy,
                                                               @RequestParam(name="sortOrder",defaultValue=AppConstants.SORT_ORDER,required=false) String sortOrder) {
        ProductResponse response=productService.getProductByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }
    @PutMapping("admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO,@PathVariable Long productId) {
        ProductDTO response=productService.updateProduct(productDTO,productId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping("admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId) {
        ProductDTO response=productService.deleteProduct(productId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage( @PathVariable Long productId,
                                                         @RequestParam("image") MultipartFile image) throws IOException {
        ProductDTO updatedProduct=productService.updateProductImage(productId,image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);

    }

}
