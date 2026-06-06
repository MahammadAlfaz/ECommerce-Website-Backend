package com.project.ecommerce.service;
import com.project.ecommerce.dto.CartDTO;
import com.project.ecommerce.dto.ProductDTO;
import com.project.ecommerce.exception.APINotFoundException;
import com.project.ecommerce.exception.ResourceNotFoundException;
import com.project.ecommerce.model.Cart;
import com.project.ecommerce.model.CartItem;
import com.project.ecommerce.model.Product;
import com.project.ecommerce.repository.CartItemRepository;
import com.project.ecommerce.repository.CartRepository;
import com.project.ecommerce.repository.ProductRepository;
import com.project.ecommerce.repository.UserRepository;
import com.project.ecommerce.util.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final AuthUtil authUtil;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;
    @Override
    public CartDTO addToCart(Long productId, Integer quantity) {
        Cart cart=createCart();
        Product product=productRepository.findById(productId).orElseThrow(
                ()->new ResourceNotFoundException("Product","productId",productId)
                );
        CartItem cartItem=cartItemRepository.findByProduct_ProductIdAndCart_CartId(
                productId,
                cart.getCartId()
        );
        if(cartItem!=null){
            throw new APINotFoundException("Product"+product.getProductName()+" already exists");
        }
        if(product.getQuantity()==0){
            throw new APINotFoundException(product.getProductName()+" is not available");
        }
        if(product.getQuantity()<quantity){
            throw new APINotFoundException("Please make an order of the "+product.getProductName() +
                    "less than equal to the quantity"+product.getQuantity());
        }
        CartItem newCartItem=new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setProductPrice(product.getSpecialPrice());
        newCartItem.setDiscountPrice(product.getDiscount());
        cartItemRepository.save(newCartItem);
        double totalPrice=product.getSpecialPrice()*quantity;
        cart.setTotalPrice(cart.getTotalPrice()+totalPrice);
        cart.getItems().add(newCartItem);
        cartRepository.save(cart);

       CartDTO cartDto=modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItems=cart.getItems();
        Stream<ProductDTO> productStream=cartItems.stream().map(
                item->{
                        ProductDTO map=modelMapper.map(item.getProduct(),ProductDTO.class);
                        map.setQuantity(item.getQuantity());
                        return map;
                }
        );
        cartDto.setProducts(productStream.toList());
        return cartDto;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if (carts.isEmpty()) {
            throw new APINotFoundException("No cart exits");
        }
        List<CartDTO> cartDTOS = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            List<ProductDTO> products = cart.getItems().stream().map(cartItem -> {
                ProductDTO productDTO = modelMapper.map(cartItem.getProduct(), ProductDTO.class);
                productDTO.setQuantity(cartItem.getQuantity()); // Set the quantity from CartItem
                return productDTO;
            }).collect(Collectors.toList());
            cartDTO.setProducts(products);

            return cartDTO;

        }).collect(Collectors.toList());

        return cartDTOS;
    }

    @Override
    public CartDTO getCartById(String emailId, Long cartId) {

        Cart cart=cartRepository.findCartByEmailAndCartId(emailId,cartId);
        if(cart==null){
            throw new ResourceNotFoundException("Cart","cartId",cartId);
        }
        CartDTO cartDTO= modelMapper.map(cart, CartDTO.class);
        cart.getItems().forEach(c->c.getProduct().setQuantity(c.getQuantity()));
        List<ProductDTO> productDTOS=cart.getItems().stream().map(
                c->modelMapper.map(c.getProduct(),ProductDTO.class)).collect(Collectors.toList());

        cartDTO.setProducts(productDTOS);
        return cartDTO;
    }

    @Override
    @Transactional
    public CartDTO updateCartProductQuantity(Long productId, Integer quantity) {

        String emailId= authUtil.loggedInEmail();
        Cart userCart=cartRepository.findByUserEmail(emailId);
        Cart cart=cartRepository.findById(userCart.getCartId())
                .orElseThrow(
                        ()->new ResourceNotFoundException("Cart","cartId",userCart.getCartId())
                );
        Product product=productRepository.findById(productId).orElseThrow(
                ()->new ResourceNotFoundException("Product","productId",productId)
        );
        CartItem cartItem= cartItemRepository.findByProduct_ProductIdAndCart_CartId(
                productId, cart.getCartId()
        );
        if(product.getQuantity()==0){
            throw new APINotFoundException(product.getProductName()+" is not available");
        }
        if(product.getQuantity()<quantity){
            throw new APINotFoundException("Please make an order of the "+product.getProductName() +
                    "less than equal to the quantity"+product.getQuantity());
        }
        if(cartItem==null){
            throw new ResourceNotFoundException("Cart","cartId",cart.getCartId());
        }
        int newQuantity=product.getQuantity()+quantity;
        if(newQuantity<0){
            throw new APINotFoundException("The quantity cannot be less than 0");
        }
        if(newQuantity==0){
            deleteProductFromCart(cart.getCartId(), productId);
        }else {
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setDiscountPrice(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * quantity));
            cartRepository.save(cart);
        }
        CartItem updatedCartItem=cartItemRepository.save(cartItem);
        if(updatedCartItem.getQuantity()==0){
            cartItemRepository.delete(updatedCartItem);
        }
        CartDTO cartDTO=modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItems=cart.getItems();
        Stream<ProductDTO> productStream=cartItems.stream().map(
                c->
                {
                    ProductDTO prd=modelMapper.map(c.getProduct(), ProductDTO.class);
                    prd.setQuantity(c.getQuantity());
                    return prd;
                }
        );
        cartDTO.setProducts(productStream.toList());

        return cartDTO;
    }

    public String deleteProductFromCart(Long cartId, long productId) {

        Cart cart = cartRepository.findById(cartId).orElseThrow(
                () -> new ResourceNotFoundException("Cart", "cartId", cartId)
        );

        CartItem cartItem = cartItemRepository
                .findByProduct_ProductIdAndCart_CartId(productId, cartId);

        if (cartItem == null) {
            throw new ResourceNotFoundException("product", "productId", productId);
        }

        cart.setTotalPrice(cart.getTotalPrice()
                - (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);

        return "Product " + cartItem.getProduct().getProductName() + " removed from cart";
    }

    public Cart createCart(){
        Cart cart=cartRepository.findByUserEmail(authUtil.loggedInEmail());
        if(cart!=null){
            return cart;
        }
        Cart newCart=new  Cart();
        newCart.setTotalPrice(0.0);
        newCart.setUser(authUtil.loggedInUser());
        cartRepository.save(newCart);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getAuthorities());
        return newCart;
    }
}
