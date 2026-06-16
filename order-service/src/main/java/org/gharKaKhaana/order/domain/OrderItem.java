package org.gharKaKhaana.order.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Long menuItemId; // Logical FK to menu-service

    @Column(nullable = false)
    private String itemName; // Snapshot of the item name at the time of order

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal itemPrice; // Snapshot of the item price at the time of order

    @Column(nullable = false)
    private Integer quantity;
}
