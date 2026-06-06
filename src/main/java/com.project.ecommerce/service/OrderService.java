package com.project.ecommerce.service;

import com.project.ecommerce.dto.OrderDTO;
import jakarta.transaction.Transactional;

@Transactional
public interface OrderService {




    @Transactional
    OrderDTO orderProducts(String paymentMethods, String email, String pgPaymentId, Long addressId, String pgResponseMessage, String pgName, String pgStatus);
}
