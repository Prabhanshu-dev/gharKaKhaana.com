package org.gharKaKhaana.service;

import org.gharKaKhaana.entity.Customer;
import org.gharKaKhaana.entity.Items;
import org.gharKaKhaana.entity.Order;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

     List<Customer> getAllCustomers();
     Boolean createCustomer(Customer customerService);
    Optional<Customer> getCustomerById(Long id);
    void deleteCustomer(Long id);
    Boolean login(String username, String password);
    Boolean signup(String username, String password);
    Boolean checkOutAndPay(String username, String password);
    Order trackOrder(Long orderId);
    Optional<Items> fetchAllItems();

}
