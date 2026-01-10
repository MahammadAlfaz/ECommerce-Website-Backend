package com.project.ecommerce.repository;

import com.project.ecommerce.model.Category;
import com.project.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository

public interface ProductRepository extends JpaRepository<Product,Long> {




    boolean existsByProductNameIgnoreCaseAndCategory_CategoryId(String productName, Long categoryId);








    Page<Product> findByCategory(Category category, Pageable pageable);

    Page<Product> findByProductNameContainsIgnoreCase(String keyword, Pageable pageable);
}
