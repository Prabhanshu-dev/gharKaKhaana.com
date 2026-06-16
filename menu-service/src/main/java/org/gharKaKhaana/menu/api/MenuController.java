package org.gharKaKhaana.menu.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gharKaKhaana.menu.application.MenuService;
import org.gharKaKhaana.menu.application.dto.MenuItemRequest;
import org.gharKaKhaana.menu.application.dto.MenuItemResponse;
import org.gharKaKhaana.menu.common.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<MenuItemResponse>>> getMenuItemsByVendor(
            @RequestParam Long vendorId,
            @RequestParam(defaultValue = "true") boolean onlyAvailable,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "" + DEFAULT_PAGE_SIZE) int size) {
        Page<MenuItemResponse> items = menuService.getMenuItemsByVendor(vendorId, onlyAvailable, PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.success("Menu items retrieved", items));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemResponse>> getMenuItem(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Menu item retrieved", menuService.getMenuItemById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MenuItemResponse>> createMenuItem(
            @RequestHeader("X-Auth-User-Id") Long userId,
            @RequestHeader("X-Auth-Role") String role,
            @Valid @RequestBody MenuItemRequest request) {
        enforceVendorRole(role);
        // Assuming userId IS vendorId logically, or vendor service mapping is needed. For now, using userId as vendorId.
        // In a real system, you might fetch vendorId using userId via inter-service call. Here we assume vendorId == userId.
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Menu item created", menuService.createMenuItem(userId, request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemResponse>> updateMenuItem(
            @PathVariable Long id,
            @RequestHeader("X-Auth-User-Id") Long userId,
            @RequestHeader("X-Auth-Role") String role,
            @Valid @RequestBody MenuItemRequest request) {
        enforceVendorRole(role);
        return ResponseEntity.ok(ApiResponse.success("Menu item updated", menuService.updateMenuItem(id, userId, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMenuItem(
            @PathVariable Long id,
            @RequestHeader("X-Auth-User-Id") Long userId,
            @RequestHeader("X-Auth-Role") String role) {
        enforceVendorRole(role);
        menuService.deleteMenuItem(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Menu item deleted", null));
    }

    private void enforceVendorRole(String role) {
        if (!"VENDOR".equalsIgnoreCase(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only vendors can modify the menu");
        }
    }
}
