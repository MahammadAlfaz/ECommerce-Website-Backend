package com.project.ecommerce.controller;

import com.project.ecommerce.dto.CartDTO;
import com.project.ecommerce.exception.ResourceNotFoundException;
import com.project.ecommerce.model.Cart;
import com.project.ecommerce.repository.CartRepository;
import com.project.ecommerce.service.CartService;
import com.project.ecommerce.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CartController {

    private final CartService cartService;
    private final CartRepository cartRepository;
    private final AuthUtil authUtil;
    @PostMapping("carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addToCart(@PathVariable Long productId,
                                             @PathVariable Integer quantity) {
    CartDTO cartDto=cartService.addToCart(productId,quantity);

    return new  ResponseEntity<CartDTO>(cartDto,HttpStatus.OK);
}
    @GetMapping("carts")
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        List<CartDTO> cartDTOS =cartService.getAllCarts();
        return new  ResponseEntity<>(cartDTOS,HttpStatus.FOUND);

    }
    @GetMapping("carts/users/cart")
    public ResponseEntity<CartDTO> getCartById() {
        String emailId= authUtil.loggedInEmail();
        Cart cart=cartRepository.findByUserEmail(emailId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "email", emailId);
        }
        Long cartId= cart.getCartId();
        CartDTO cartDTO=cartService.getCartById(emailId,cartId);
        return new ResponseEntity<>(cartDTO,HttpStatus.OK);
    }
    @PutMapping("cart/products/{productId}/quantity/{operation}")
public ResponseEntity<CartDTO> updateCartProductQuantity(@PathVariable Long productId,
                                                         @PathVariable String operation) {
        CartDTO cartDTO=cartService.updateCartProductQuantity(productId,
                operation.equalsIgnoreCase("delete")?-1:1);
        return new ResponseEntity<>(cartDTO,HttpStatus.OK);

}
@DeleteMapping("carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId,
                                                        @PathVariable long productId) {
        String response= cartService.deleteProductFromCart(cartId,productId);
        return new ResponseEntity<>(response, HttpStatus.OK);


}

}
