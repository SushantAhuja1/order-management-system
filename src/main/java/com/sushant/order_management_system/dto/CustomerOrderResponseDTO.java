package com.sushant.order_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerOrderResponseDTO {
    private Long id;
    private String productName;
    private Double price;
    private Integer quantity;
    private Long customerId;
    private String customerName;
}