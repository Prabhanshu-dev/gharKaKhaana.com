package org.himalayas.service.ServiceImpl;


import org.himalayas.entity.Customer;
import org.himalayas.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
     @Override
     public List<CustomerService> getAllCustomers() {
          return List.of();
     }

     @Override
     public CustomerService createCustomer(Customer customerService) {
          return null;
     }

     @Override
     public CustomerService getCustomerById(Long id) {
          return null;
     }

     @Override
     public void deleteCustomer(Long id) {

     }

     // Implement methods from the Customer interface here



}
