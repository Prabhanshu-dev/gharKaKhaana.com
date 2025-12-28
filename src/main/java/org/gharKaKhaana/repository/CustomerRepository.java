package org.gharKaKhaana.repository;

import org.gharKaKhaana.entity.Customer;
import org.gharKaKhaana.entity.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c WHERE c.name = :username AND c.password = :password")
    Optional<Customer> findByUsernameAndPassword(String username, String password);

    @Query("SELECT c FROM Customer c WHERE c.name = :username")
    Optional<Customer> findByName(String username);

    @Query("Select i  FROM Items i")
    Optional<Items> fetchAllItems();
}
