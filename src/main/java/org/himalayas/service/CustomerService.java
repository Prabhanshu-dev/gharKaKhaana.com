package org.himalayas.service;

import org.himalayas.entity.Customer;
import org.himalayas.entity.Order;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

     List<Customer> getAllCustomers();
     Customer createCustomer(Customer customerService);
    Optional<Customer> getCustomerById(Long id);
    void deleteCustomer(Long id);
    Boolean login(String username, String password);
    Boolean signup(String username, String password);
    Boolean checkOutAndPay(String username, String password);
    Order trackOrder(String orderId);

}
