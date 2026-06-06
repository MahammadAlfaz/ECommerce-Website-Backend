package com.project.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    private Double totalPrice=0.0;
    @OneToOne
    @JoinColumn(name="user_id")
    private User user;
    @Getter
    @Setter
    @OneToMany(mappedBy = "cart", cascade = {CascadeType.MERGE, CascadeType.PERSIST},orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();
}
