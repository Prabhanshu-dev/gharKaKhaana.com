package org.himalayas.service;

import org.himalayas.entity.Customer;
import org.himalayas.entity.Order;

import java.util.List;

public interface CustomerService {

     List<Customer> getAllCustomers();
     Customer createCustomer(Customer customerService);
    Customer getCustomerById(Long id);
    void deleteCustomer(Long id);
    Boolean login(String username, String password);
    Boolean signup(String username, String password);
    Boolean checkOutAndPay(String username, String password);
    Order trackOrder(String orderId);

}
