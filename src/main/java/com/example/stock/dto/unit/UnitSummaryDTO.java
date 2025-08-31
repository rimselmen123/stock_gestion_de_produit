package com.example.stock.dto.unit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Unit summary information.
 * Contains minimal unit data for listings and dropdowns.
 * Optimized for performance when full details are not needed.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnitSummaryDTO {

    /**
     * The unique identifier of the unit.
     * UUID format string.
     */
    private String id;

    /**
     * The name of the unit.
     */
    private String name;

    /**
     * The symbol of the unit.
     */
    private String symbol;
}
