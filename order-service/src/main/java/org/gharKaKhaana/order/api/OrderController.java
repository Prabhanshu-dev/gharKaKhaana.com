package org.gharKaKhaana.order.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gharKaKhaana.order.application.OrderService;
import org.gharKaKhaana.order.application.dto.OrderResponse;
import org.gharKaKhaana.order.application.dto.PlaceOrderRequest;
import org.gharKaKhaana.order.common.ApiResponse;
import org.gharKaKhaana.order.domain.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(
            @RequestHeader("X-Auth-User-Id") Long userId,
            @RequestHeader("X-Auth-Role") String role,
            @Valid @RequestBody PlaceOrderRequest request) {
        enforceCustomerRole(role);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order placed successfully", orderService.placeOrder(userId, request)));
    }

    @GetMapping("/customer")
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getMyOrders(
            @RequestHeader("X-Auth-User-Id") Long userId,
            @RequestHeader("X-Auth-Role") String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "" + DEFAULT_PAGE_SIZE) int size) {
        enforceCustomerRole(role);
        Page<OrderResponse> orders = orderService.getOrdersByUser(userId, PageRequest.of(page, size, Sort.by("createdAt").descending()));
        return ResponseEntity.ok(ApiResponse.success("Orders retrieved", orders));
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderForCustomer(
            @PathVariable Long id,
            @RequestHeader("X-Auth-User-Id") Long userId,
            @RequestHeader("X-Auth-Role") String role) {
        enforceCustomerRole(role);
        return ResponseEntity.ok(ApiResponse.success("Order retrieved", orderService.getOrderByIdAndUser(id, userId)));
    }

    @PutMapping("/customer/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @PathVariable Long id,
            @RequestHeader("X-Auth-User-Id") Long userId,
            @RequestHeader("X-Auth-Role") String role) {
        enforceCustomerRole(role);
        return ResponseEntity.ok(ApiResponse.success("Order cancelled", orderService.cancelOrder(id, userId)));
    }

    @GetMapping("/vendor")
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getVendorOrders(
            @RequestHeader("X-Auth-User-Id") Long vendorId,
            @RequestHeader("X-Auth-Role") String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "" + DEFAULT_PAGE_SIZE) int size) {
        enforceVendorRole(role);
        Page<OrderResponse> orders = orderService.getOrdersByVendor(vendorId, PageRequest.of(page, size, Sort.by("createdAt").descending()));
        return ResponseEntity.ok(ApiResponse.success("Orders retrieved", orders));
    }

    @GetMapping("/vendor/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderForVendor(
            @PathVariable Long id,
            @RequestHeader("X-Auth-User-Id") Long vendorId,
            @RequestHeader("X-Auth-Role") String role) {
        enforceVendorRole(role);
        return ResponseEntity.ok(ApiResponse.success("Order retrieved", orderService.getOrderByIdAndVendor(id, vendorId)));
    }

    @PutMapping("/vendor/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status,
            @RequestHeader("X-Auth-User-Id") Long vendorId,
            @RequestHeader("X-Auth-Role") String role) {
        enforceVendorRole(role);
        return ResponseEntity.ok(ApiResponse.success("Order status updated", orderService.updateOrderStatus(id, vendorId, status)));
    }

    private void enforceCustomerRole(String role) {
        if (!"CUSTOMER".equalsIgnoreCase(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only customers can perform this action");
        }
    }

    private void enforceVendorRole(String role) {
        if (!"VENDOR".equalsIgnoreCase(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only vendors can perform this action");
        }
    }
}
