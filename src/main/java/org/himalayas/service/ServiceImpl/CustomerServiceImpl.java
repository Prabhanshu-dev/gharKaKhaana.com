package org.himalayas.service.ServiceImpl;


import org.himalayas.entity.Customer;
import org.himalayas.entity.Order;
import org.himalayas.repository.CustomerRepository;
import org.himalayas.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }


    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public Boolean login(String username, String password) {
        Customer customer = customerRepository.findByUsername(username);
        // Placeholder: check password (assuming Customer has getPassword())
        return customer != null && "password".equals(password);
    }

    @Override
    public Boolean signup(String username, String password) {
        Customer existing = customerRepository.findByUsername(username);
        if (existing != null) return false;
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setPassword(password);
        customerRepository.save(customer);
        return true;
    }

    @Override
    public Boolean checkOutAndPay(String username, String password) {
        // Placeholder: just check login
        return login(username, password);
    }

    @Override
    public Order trackOrder(String orderId) {
        // Placeholder logic for tracking order
        return new Order(orderId, "In Transit");
    }

     // Implement methods from the Customer interface here



}
