package com.project.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    @OneToOne(mappedBy = "payment" , cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Order order;
    @NotBlank
    @Size(min=4,message = "payment method should at least has 4 character")
    private String paymentMethod;

    private String pgPaymentId;
    private  String pgPaymentStatus;
    private String pgResponseMessage;
    private String pgName;

    public Payment(String paymentMethod, String pgPaymentId, String pgPaymentStatus, String pgResponseMessage, String pgName) {
        this.paymentMethod = paymentMethod;
        this.pgPaymentId = pgPaymentId;
        this.pgPaymentStatus = pgPaymentStatus;
        this.pgResponseMessage = pgResponseMessage;
        this.pgName = pgName;
    }
}
