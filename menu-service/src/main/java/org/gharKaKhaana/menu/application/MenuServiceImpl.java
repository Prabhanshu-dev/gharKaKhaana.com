package org.gharKaKhaana.menu.application;

import lombok.RequiredArgsConstructor;
import org.gharKaKhaana.menu.application.dto.MenuItemRequest;
import org.gharKaKhaana.menu.application.dto.MenuItemResponse;
import org.gharKaKhaana.menu.common.exception.MenuItemNotFoundException;
import org.gharKaKhaana.menu.domain.MenuItem;
import org.gharKaKhaana.menu.infrastructure.MenuItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    
    private final MenuItemRepository menuItemRepository;

    @Override
    public MenuItemResponse createMenuItem(Long vendorId, MenuItemRequest request) {
        MenuItem item = MenuItem.builder()
                .vendorId(vendorId)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .isAvailable(request.isAvailable())
                .imageUrl(request.getImageUrl())
                .build();
        return toResponse(menuItemRepository.save(item));
    }

    @Override
    public MenuItemResponse updateMenuItem(Long id, Long vendorId, MenuItemRequest request) {
        MenuItem item = menuItemRepository.findByIdAndVendorId(id, vendorId)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu item not found or unauthorized"));
                
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setCategory(request.getCategory());
        item.setAvailable(request.isAvailable());
        item.setImageUrl(request.getImageUrl());
        
        return toResponse(menuItemRepository.save(item));
    }

    @Override
    public void deleteMenuItem(Long id, Long vendorId) {
        MenuItem item = menuItemRepository.findByIdAndVendorId(id, vendorId)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu item not found or unauthorized"));
        menuItemRepository.delete(item);
    }

    @Override
    public MenuItemResponse getMenuItemById(Long id) {
        return menuItemRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu item not found: " + id));
    }

    @Override
    public Page<MenuItemResponse> getMenuItemsByVendor(Long vendorId, boolean onlyAvailable, Pageable pageable) {
        if (onlyAvailable) {
            return menuItemRepository.findByVendorIdAndIsAvailableTrue(vendorId, pageable)
                    .map(this::toResponse);
        }
        return menuItemRepository.findByVendorId(vendorId, pageable)
                .map(this::toResponse);
    }
    
    private MenuItemResponse toResponse(MenuItem item) {
        return MenuItemResponse.builder()
                .id(item.getId())
                .vendorId(item.getVendorId())
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .category(item.getCategory())
                .isAvailable(item.isAvailable())
                .imageUrl(item.getImageUrl())
                .createdAt(item.getCreatedAt())
                .build();
    }
}
