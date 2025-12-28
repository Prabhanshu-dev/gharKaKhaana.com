package org.gharKaKhaana.service.ServiceImpl;


import org.gharKaKhaana.entity.Customer;
import org.gharKaKhaana.entity.Items;
import org.gharKaKhaana.entity.Order;
import org.gharKaKhaana.repository.CustomerRepository;
import org.gharKaKhaana.service.CustomerService;
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
    public Boolean createCustomer(Customer customer) {
        try{
            Customer  result = customerRepository.save(customer);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
        Optional<Customer> customer = customerRepository.findByUsernameAndPassword(username,password);
        // Placeholder: check password (assuming Customer has getPassword())
        return customer != null && "password".equals(password);
    }

    @Override
    public Boolean signup(String username, String password) {
        Optional<Customer> existing = customerRepository.findByName(username);
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
    public Order trackOrder(Long orderId) {
        // Placeholder logic for tracking order
        return new Order(String.valueOf(orderId), "In Transit");
    }

    @Override
    public Optional<Items> fetchAllItems() {
        return customerRepository.fetchAllItems();
    }

    // Implement methods from the Customer interface here



}
