package com.sushant.order_management_system.service;

import com.sushant.order_management_system.dto.CustomerDTO;
import com.sushant.order_management_system.entity.Customer;
import com.sushant.order_management_system.entity.Customer_Order;
import com.sushant.order_management_system.repository.CustomerOrderRepository;
import com.sushant.order_management_system.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final ModelMapper modelMapper;
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        for(Customer customer : customers) {
            customerDTOList.add(modelMapper.map(customer, CustomerDTO.class));
        }
        return customerDTOList;
    }
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        modelMapper.map(customerDTO, customer);
        List<Customer_Order> customerOrders = customer.getOrders();
        for(Customer_Order customerOrder : customerOrders) {
            customerOrder.setCustomer(customer);
        }
        Customer savedCustomer = customerRepository.save(customer);
        return modelMapper.map(savedCustomer, CustomerDTO.class);
    }
    //excel-code
    public byte[] exportCustomerToExcel() throws IOException {
        List<CustomerDTO> customers = getAllCustomers();
        try(Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Orders Report");
            //Header Row
            String[] headers = {"Customer ID", "Customer Name", "Email", "Product", "Price", "Quantity", "Order Id"};
            Row headerRow = sheet.createRow(0);
            for(int i=0; i<headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            //fill-data-rows
            int rowIndex=1;
            for(CustomerDTO customer : customers) {
                if(customer.getOrders()!=null) {
                    for(var order : customer.getOrders()) {
                        Row row = sheet.createRow(rowIndex++);
                        row.createCell(0).setCellValue(customer.getId());
                        row.createCell(1).setCellValue(customer.getName());
                        row.createCell(2).setCellValue(customer.getEmail());
                        row.createCell(3).setCellValue(order.getProductName());
                        row.createCell(4).setCellValue(order.getPrice());
                        row.createCell(5).setCellValue(order.getQuantity());
                        row.createCell(6).setCellValue(order.getId());
                    }
                }
            }
            workbook.write(out);
            workbook.close();
            return out.toByteArray();
        }
    }
}