package com.project.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private long orderItemId;
    private ProductDTO product;
    private Integer quantity;
    private Double discount;
    private Double orderedTotalPrice;

}
