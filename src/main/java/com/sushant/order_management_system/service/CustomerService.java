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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
            int rowIndex=0;
            //Main Heading
            Row titleRow = sheet.createRow(rowIndex++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Customer Order Report");
            //merge the cells
            sheet.addMergedRegion(new CellRangeAddress(0,0,0,6));
            //blank row
            rowIndex++;
            //printing date in MM-dd-yyyy format
            Row dateRow = sheet.createRow(rowIndex++);
            String currentFormattedDate = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
            dateRow.createCell(0).setCellValue("Printed Date : "+currentFormattedDate);
            //blank row
            rowIndex++;
            //Header Row
            String[] headers = {"Customer ID", "Customer Name", "Email", "Product", "Price", "Quantity", "Order Id"};
            Row headerRow = sheet.createRow(rowIndex++);
            for(int i=0; i<headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            //fill-data-rows
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