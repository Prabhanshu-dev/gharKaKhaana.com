package org.himalayas.service;

import org.himalayas.entity.Customer;

import java.util.List;

public interface CustomerService {

     List<CustomerService> getAllCustomers();
     CustomerService createCustomer(Customer customerService);
    CustomerService getCustomerById(Long id);
    void deleteCustomer(Long id);

}
