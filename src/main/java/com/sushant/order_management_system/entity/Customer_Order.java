package com.sushant.order_management_system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Customer_Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private Double price;
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}