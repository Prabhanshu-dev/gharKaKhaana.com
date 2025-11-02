package org.himalayas.repository;

import org.himalayas.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByEmail(String email);

    void deleteById(Long id);

    Customer findByUsername(String username);

    List<Customer> findAll();

    Customer save(Customer customer);
}
