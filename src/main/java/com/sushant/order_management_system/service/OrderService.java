package com.sushant.order_management_system.service;

import com.sushant.order_management_system.dto.CustomerOrderResponseDTO;
import com.sushant.order_management_system.entity.Customer_Order;
import com.sushant.order_management_system.repository.CustomerOrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CustomerOrderRepository  customerOrderRepository;
    private final ModelMapper  modelMapper;
    public List<CustomerOrderResponseDTO> getOrdersByProductName(String productName) {
        List<Customer_Order> customerOrders = customerOrderRepository.findByProductNameContaining(productName);
        List<CustomerOrderResponseDTO> customerOrderDTOList = new ArrayList<>();
        for(Customer_Order customerOrder : customerOrders){
            CustomerOrderResponseDTO dto = modelMapper.map(customerOrder, CustomerOrderResponseDTO.class);
            if(customerOrder.getCustomer()!=null) {
                dto.setCustomerName(customerOrder.getCustomer().getName());
                dto.setCustomerId(customerOrder.getCustomer().getId());
            }
            customerOrderDTOList.add(dto);
        }
        return customerOrderDTOList;
    }
}