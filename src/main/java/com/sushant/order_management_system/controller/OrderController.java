package com.sushant.order_management_system.controller;

import com.sushant.order_management_system.dto.CustomerOrderResponseDTO;
import com.sushant.order_management_system.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @GetMapping("/search/{name}")
    public List<CustomerOrderResponseDTO> getAllOrders(@PathVariable String name) {
        return orderService.getOrdersByProductName(name);
    }
}
