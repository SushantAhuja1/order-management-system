package com.sushant.order_management_system.repository;

import com.sushant.order_management_system.entity.Customer_Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<Customer_Order,Long> {
    List<Customer_Order> findByProductNameContaining(String productName);
    @Query(value = "SELECT c.id,c.name,c.email,o.product_name,o.price,o.quantity,o.id AS order_id FROM customer AS c INNER JOIN customer_order o ON c.id = o.customer_id", nativeQuery = true)
    List<Object[]> customQueryJoin();
    @Query(value = "SELECT c.id,c.name,o.product_name,o.price,o.id AS order_id FROM customer AS c INNER JOIN customer_order AS o ON c.id = o.customer_id WHERE c.id=:customerId",nativeQuery = true)
    List<Object[]> customQueryForACustomer(Long customerId);
}