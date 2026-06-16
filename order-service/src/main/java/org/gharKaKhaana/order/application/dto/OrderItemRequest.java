package org.gharKaKhaana.order.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemRequest {
    @NotNull(message = "Menu item ID is required")
    private Long menuItemId;

    @NotBlank(message = "Item name snapshot is required")
    private String itemName;

    @NotNull(message = "Item price snapshot is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal itemPrice;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
