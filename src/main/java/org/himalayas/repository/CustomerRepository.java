package org.himalayas.repository;

import org.himalayas.entity.Customer;
import java.util.List;

public interface CustomerRepository {
    List<Customer> findAll();
    Customer save(Customer customer);
    Customer findById(Long id);
    void deleteById(Long id);
    Customer findByUsername(String username);
}

