package org.himalayas.controller;

import org.himalayas.entity.Customer;
import org.himalayas.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gkk/v1/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping("/customers")
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @GetMapping("/login")
    public Boolean login(@RequestParam String username, @RequestParam String password) {
        // Logic to authenticate user
        // This is a placeholder; implement your authentication logic here
        return "admin".equals(username) && "password".equals(password);
    }

    @PostMapping("/signup")
    public Boolean signup(@RequestParam String username, @RequestParam String password) {
        // Logic to register a new user
        // This is a placeholder; implement your registration logic here
        return true; // Assume registration is successful
    }

    @PostMapping("/checkout")
    public Boolean checkOutAndPay(@RequestParam String username, @RequestParam String password) {
        // Logic to check out and pay
        // This is a placeholder; implement your checkout logic here
        return "admin".equals(username) && "password".equals(password);
    }

    @GetMapping("/trackOrder")
    public Order trackOrder(@RequestParam String orderId) {
        // Logic to track an order
        // This is a placeholder; implement your order tracking logic here
        return new Order(orderId, "In Transit");
    }
    // Add more methods as needed for your application
}
