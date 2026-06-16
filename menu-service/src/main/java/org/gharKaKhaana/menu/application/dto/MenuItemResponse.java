package org.gharKaKhaana.menu.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gharKaKhaana.menu.domain.MenuCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemResponse {
    private Long id;
    private Long vendorId;
    private String name;
    private String description;
    private BigDecimal price;
    private MenuCategory category;
    private boolean isAvailable;
    private String imageUrl;
    private LocalDateTime createdAt;
}
