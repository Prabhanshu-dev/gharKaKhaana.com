package org.gharKaKhaana.menu.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.gharKaKhaana.menu.domain.MenuCategory;

import java.math.BigDecimal;

@Data
public class MenuItemRequest {
    @NotBlank(message = "Item name is required")
    @Size(min = 2, max = 150)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @NotNull(message = "Category is required")
    private MenuCategory category;

    private boolean isAvailable;
    
    @Size(max = 500)
    private String imageUrl;
}
