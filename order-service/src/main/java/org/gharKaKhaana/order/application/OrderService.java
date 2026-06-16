package org.gharKaKhaana.order.application;

import org.gharKaKhaana.order.application.dto.OrderResponse;
import org.gharKaKhaana.order.application.dto.PlaceOrderRequest;
import org.gharKaKhaana.order.domain.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponse placeOrder(Long userId, PlaceOrderRequest request);
    OrderResponse getOrderByIdAndUser(Long id, Long userId);
    OrderResponse getOrderByIdAndVendor(Long id, Long vendorId);
    Page<OrderResponse> getOrdersByUser(Long userId, Pageable pageable);
    Page<OrderResponse> getOrdersByVendor(Long vendorId, Pageable pageable);
    OrderResponse updateOrderStatus(Long orderId, Long vendorId, OrderStatus newStatus);
    OrderResponse cancelOrder(Long orderId, Long userId);
}
