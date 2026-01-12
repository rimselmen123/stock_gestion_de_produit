package com.example.stock.dto.variation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemVariationResponseDTO {
    private Long id;
    private Long menuItemSnapshotId;
    private String name;
}