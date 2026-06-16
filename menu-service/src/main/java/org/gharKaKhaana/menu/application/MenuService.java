package org.gharKaKhaana.menu.application;

import org.gharKaKhaana.menu.application.dto.MenuItemRequest;
import org.gharKaKhaana.menu.application.dto.MenuItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MenuService {
    MenuItemResponse createMenuItem(Long vendorId, MenuItemRequest request);
    MenuItemResponse updateMenuItem(Long id, Long vendorId, MenuItemRequest request);
    void deleteMenuItem(Long id, Long vendorId);
    MenuItemResponse getMenuItemById(Long id);
    Page<MenuItemResponse> getMenuItemsByVendor(Long vendorId, boolean onlyAvailable, Pageable pageable);
}
