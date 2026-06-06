package com.project.ecommerce.controller;

import com.project.ecommerce.dto.OrderDTO;
import com.project.ecommerce.dto.OrderRequestDTO;
import com.project.ecommerce.service.OrderService;
import com.project.ecommerce.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private  final AuthUtil authUtil;
    @PostMapping("order/users/payments/{paymentMethods}")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable String paymentMethods,
                                                  @RequestBody OrderRequestDTO orderRequestDTO
                                                  ) {
        String email= authUtil.loggedInEmail();
        OrderDTO dto=orderService.orderProducts(paymentMethods,
                email,
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getAddressId(),
                orderRequestDTO.getPgResponse(),
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgStatus());
        return new ResponseEntity<>(dto, HttpStatus.OK);

    }
}
