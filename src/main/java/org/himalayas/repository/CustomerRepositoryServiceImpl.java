package org.himalayas.repository;

import org.himalayas.entity.Customer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomerRepositoryServiceImpl implements CustomerRepository {
    private final Map<Long, Customer> customerStore = new HashMap<>();
    private long idCounter = 1;

    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(customerStore.values());
    }

    @Override
    public Customer save(Customer customer) {
        if (customer.getId() == null) {
            customer.setId(idCounter++);
        }
        customerStore.put(customer.getId(), customer);
        return customer;
    }

    @Override
    public Customer findById(Long id) {
        return customerStore.get(id);
    }

    @Override
    public void deleteById(Long id) {
        customerStore.remove(id);
    }

    @Override
    public Customer findByUsername(String username) {
        // Using email as username since Customer does not have a username field
        return customerStore.values().stream()
                .filter(c -> username.equals(c.getEmail()))
                .findFirst()
                .orElse(null);
    }
}
