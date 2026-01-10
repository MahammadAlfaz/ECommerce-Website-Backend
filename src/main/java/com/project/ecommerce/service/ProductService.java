package com.project.ecommerce.service;

import com.project.ecommerce.dto.ProductDTO;
import com.project.ecommerce.dto.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(ProductDTO productDTO, Long categoryId);

    ProductResponse getAllProduct(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);

    ProductResponse getProductByCategory(Long categoryId,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);

    ProductResponse getProductByKeyword(String keyword,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);

    ProductDTO updateProduct(ProductDTO productDTO, Long productId);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
