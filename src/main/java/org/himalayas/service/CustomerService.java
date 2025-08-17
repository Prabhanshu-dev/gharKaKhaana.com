package org.himalayas.service;

import org.himalayas.entity.Customer;

import java.util.List;

public interface CustomerService {

     List<Customer> getAllCustomers();
     Customer createCustomer(Customer customerService);
    Customer getCustomerById(Long id);
    void deleteCustomer(Long id);

}
