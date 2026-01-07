package com.project.ecommerce.repository;

import com.project.ecommerce.model.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    String findByCategoryName(@NotEmpty(message = "Category name can't be blank") @Size(min = 5,message = "Category name should be more than 4 character") String categoryName);

    boolean existsByCategoryName(@NotEmpty(message = "Category name can't be blank") @Size(min = 5,message = "Category name should be more than 4 character") String categoryName);
}
