package com.project.ecommerce.dto;

import com.project.ecommerce.model.Order;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Long paymentId;

    private String paymentMethod;
    private String pgPaymentId;
    private  String pgPaymentStatus;
    private String pgResponseMessage;
    private String pgName;

}
