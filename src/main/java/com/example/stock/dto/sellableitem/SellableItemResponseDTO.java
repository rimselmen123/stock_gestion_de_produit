package com.example.stock.dto.sellableitem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellableItemResponseDTO {
    private Long id;
    private Long menuItemSnapshotId;
    private Long menuItemVariationSnapshotId; // NULL si pas de variation
    private String name;
}