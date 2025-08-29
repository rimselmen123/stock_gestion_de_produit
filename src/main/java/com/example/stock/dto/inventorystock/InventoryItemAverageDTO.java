package com.example.stock.dto.inventorystock;

import com.example.stock.repository.projection.ItemAverageProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItemAverageDTO {
    private String id;
    private String name;
    private BigDecimal moyenne;

    public static InventoryItemAverageDTO fromProjection(ItemAverageProjection p) {
        return InventoryItemAverageDTO.builder()
                .id(p.getInventoryItemId())
                .name(p.getName())
                .moyenne(p.getMoyenne())
                .build();
    }
}
