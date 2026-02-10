package com.sushant.order_management_system.repository;

import com.sushant.order_management_system.entity.Customer_Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<Customer_Order,Long> {
    List<Customer_Order> findByProductNameContaining(String productName);
}