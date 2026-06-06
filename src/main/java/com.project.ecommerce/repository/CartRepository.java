package com.project.ecommerce.repository;

import com.project.ecommerce.model.Cart;
import com.project.ecommerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c WHERE c.user.email = :email")
    Cart findByUserEmail(@Param("email") String email);

@Query("SELECT c FROM Cart c WHERE c.user.email =?1 AND c.cartId=?2")
    Cart findCartByEmailAndCartId(String emailId, Long cartId);


}
