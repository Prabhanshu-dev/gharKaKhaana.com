package org.gharKaKhaana.controller;

import org.gharKaKhaana.entity.Customer;
import org.gharKaKhaana.entity.Items;
import org.gharKaKhaana.entity.Order;
import org.gharKaKhaana.service.CustomerService;
import org.gharKaKhaana.service.UpiPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gkk/v1/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    private UpiPaymentService upiPaymentService;

    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping("/customers")
    public Boolean createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @GetMapping("/login")
    public Boolean login(@RequestParam String username, @RequestParam String password) {
        return "admin".equals(username) && "password".equals(password);
    }

    @PostMapping("/signup")
    public Boolean signup(@RequestParam String username, @RequestParam String password) {
          customerService.signup(username,password);
          return true;
    }

    @PostMapping("/checkout")
    public Boolean checkOutAndPay(@RequestParam String upiId, @RequestParam double amount) {
        // Simulate UPI payment
        return upiPaymentService.processPayment(upiId, amount);
    }

    @GetMapping("/trackOrder")
    public Order trackOrder(@RequestParam Long orderId) {
        return new Order(String.valueOf(orderId), "In Transit");
    }


    @GetMapping("/fetchAllDishes")
    public Items fetchAllDishes() {
        // Logic to fetch all dishes
        return customerService.fetchAllItems().orElse(null);
    }
}
