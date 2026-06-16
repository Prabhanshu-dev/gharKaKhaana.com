package org.gharKaKhaana.menu.infrastructure;

import org.gharKaKhaana.menu.domain.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    Page<MenuItem> findByVendorId(Long vendorId, Pageable pageable);
    Page<MenuItem> findByVendorIdAndIsAvailableTrue(Long vendorId, Pageable pageable);
    Optional<MenuItem> findByIdAndVendorId(Long id, Long vendorId);
}
