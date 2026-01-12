package com.example.stock.dto.variation;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariationDTO {
    private Long id;
    private String variation;
    private Long menu_item_id;
}