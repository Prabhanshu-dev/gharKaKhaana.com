package org.gharKaKhaana.order.application;

import lombok.RequiredArgsConstructor;
import org.gharKaKhaana.order.application.dto.OrderItemResponse;
import org.gharKaKhaana.order.application.dto.OrderResponse;
import org.gharKaKhaana.order.application.dto.PlaceOrderRequest;
import org.gharKaKhaana.order.common.exception.OrderNotFoundException;
import org.gharKaKhaana.order.domain.Order;
import org.gharKaKhaana.order.domain.OrderItem;
import org.gharKaKhaana.order.domain.OrderStatus;
import org.gharKaKhaana.order.infrastructure.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderResponse placeOrder(Long userId, PlaceOrderRequest request) {
        BigDecimal totalAmount = request.getItems().stream()
                .map(item -> item.getItemPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .userId(userId)
                .vendorId(request.getVendorId())
                .status(OrderStatus.PLACED)
                .totalAmount(totalAmount)
                .build();

        request.getItems().forEach(itemRequest -> {
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .menuItemId(itemRequest.getMenuItemId())
                    .itemName(itemRequest.getItemName())
                    .itemPrice(itemRequest.getItemPrice())
                    .quantity(itemRequest.getQuantity())
                    .build();
            order.getItems().add(orderItem);
        });

        return toResponse(orderRepository.save(order));
    }

    @Override
    public OrderResponse getOrderByIdAndUser(Long id, Long userId) {
        return orderRepository.findByIdAndUserId(id, userId)
                .map(this::toResponse)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }

    @Override
    public OrderResponse getOrderByIdAndVendor(Long id, Long vendorId) {
        return orderRepository.findByIdAndVendorId(id, vendorId)
                .map(this::toResponse)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }

    @Override
    public Page<OrderResponse> getOrdersByUser(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable).map(this::toResponse);
    }

    @Override
    public Page<OrderResponse> getOrdersByVendor(Long vendorId, Pageable pageable) {
        return orderRepository.findByVendorId(vendorId, pageable).map(this::toResponse);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, Long vendorId, OrderStatus newStatus) {
        Order order = orderRepository.findByIdAndVendorId(orderId, vendorId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.setStatus(newStatus);
        return toResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long orderId, Long userId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        
        if (order.getStatus() != OrderStatus.PLACED) {
            throw new IllegalStateException("Only PLACED orders can be cancelled by user");
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        return toResponse(orderRepository.save(order));
    }

    private OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .vendorId(order.getVendorId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(order.getItems().stream().map(this::toItemResponse).collect(Collectors.toList()))
                .build();
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .id(item.getId())
                .menuItemId(item.getMenuItemId())
                .itemName(item.getItemName())
                .itemPrice(item.getItemPrice())
                .quantity(item.getQuantity())
                .build();
    }
}
