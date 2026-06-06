package com.project.ecommerce.service;

import com.project.ecommerce.dto.CartDTO;
import com.project.ecommerce.model.Cart;

import java.util.List;

public interface CartService {
    CartDTO addToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCartById(String emailId, Long cartId);

    CartDTO updateCartProductQuantity(Long productId, Integer quantity);

    String deleteProductFromCart(Long cartId, long productId);
}
