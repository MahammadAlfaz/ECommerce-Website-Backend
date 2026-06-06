package com.project.ecommerce.repository;

import com.project.ecommerce.model.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {


    CartItem findByProduct_ProductIdAndCart_CartId(Long productId, Long cartId);


    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem c WHERE c.cart.cartId = :cartId AND c.product.productId = :productId")
    void deleteCartItemByProductIdAndCartId(@Param("cartId") Long cartId,
                                            @Param("productId") Long productId);
}
