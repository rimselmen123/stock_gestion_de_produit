package com.example.stock.dto.brand;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Brand summary information.
 * Contains minimal brand data for listings and dropdowns.
 * Optimized for performance when full details are not needed.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandSummaryDTO {

    /**
     * The unique identifier of the brand.
     * UUID format string.
     */
    private String id;

    /**
     * The name of the brand.
     */
    private String name;

    /**
     * URL to the brand's image/logo.
     * Useful for displaying brand logos in lists.
     */
    private String imageUrl;
}
