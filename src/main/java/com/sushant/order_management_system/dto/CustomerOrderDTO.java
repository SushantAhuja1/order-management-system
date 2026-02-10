package com.sushant.order_management_system.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerOrderDTO {
    private Long id;
    private String productName;
    private Double price;
    private Integer quantity;
}
