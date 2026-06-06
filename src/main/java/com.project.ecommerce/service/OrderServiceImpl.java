package com.project.ecommerce.service;

import com.project.ecommerce.dto.OrderDTO;

import com.project.ecommerce.dto.OrderItemDTO;
import com.project.ecommerce.exception.APINotFoundException;
import com.project.ecommerce.exception.ResourceNotFoundException;
import com.project.ecommerce.model.*;
import com.project.ecommerce.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final PaymentRepository paymentRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;


    @Transactional
    @Override
    public OrderDTO orderProducts(String paymentMethods,
                                  String email,
                                  String pgPaymentId,
                                  Long addressId,
                                  String pgResponseMessage,
                                  String pgName,
                                  String pgStatus) {

        Cart cart = cartRepository.findByUserEmail(email);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "email", email);
        }

        List<CartItem> cartItems = cart.getItems();
        if (cartItems.isEmpty()) {
            throw new APINotFoundException("Cart is empty");
        }

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));

        // ------------------ CREATE ORDER ------------------
        Order order = new Order();
        order.setEmail(email);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order accepted !");
        order.setAddress(address);

        Order savedOrder = orderRepository.save(order);

        // ------------------ SAVE PAYMENT ------------------
        Payment payment = new Payment(paymentMethods, pgPaymentId, pgStatus, pgResponseMessage, pgName);
        payment.setOrder(savedOrder);
        payment = paymentRepository.save(payment);

        savedOrder.setPayment(payment);

        // ------------------ CREATE ORDER ITEMS ------------------
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {

            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();

            if (product.getQuantity() < quantity) {
                throw new APINotFoundException(product.getProductName() + " is out of stock");
            }

            OrderItem orderItem = new OrderItem();

            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setDiscount(cartItem.getDiscountPrice());
            orderItem.setOrderProductPrice(cartItem.getProductPrice());
            orderItem.setOrderedTotalPrice(cartItem.getProductPrice() * quantity);
            orderItem.setOrder(savedOrder);

            orderItems.add(orderItem);
        }

        List<OrderItem> savedOrderItems = orderItemRepository.saveAll(orderItems);

        // ------------------ UPDATE PRODUCT STOCK & CLEAR CART SAFELY ------------------
        List<CartItem> cartItemsCopy = new ArrayList<>(cartItems);

        cartItemsCopy.forEach(item -> {
            Product product = item.getProduct();
            int qty = item.getQuantity();

            product.setQuantity(product.getQuantity() - qty);
            productRepository.save(product);

            cartService.deleteProductFromCart(cart.getCartId(), product.getProductId());
        });

        // ------------------ MAP RESPONSE ------------------
        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);

        List<OrderItemDTO> orderItemDTOList = savedOrderItems.stream()
                .map(item -> modelMapper.map(item, OrderItemDTO.class))
                .toList();

        orderDTO.setOrderItem(orderItemDTOList);
        orderDTO.setTotalPrice(savedOrder.getTotalAmount());

        return orderDTO;
    }
}