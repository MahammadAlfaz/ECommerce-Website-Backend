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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    @NotBlank
    @Size(min=3,max=50,message = "Product name should be more than 2 character")
    private String productName;
    private String  image;
    @NotBlank
    @Size(min=6,message = "Product description should be more than 5 character")
    private String description;
    private Integer quantity;
    private double price;
    private double specialPrice;
    private double discount;
    @ManyToOne
    @JoinColumn(name ="category_id")
    private Category category;



}
