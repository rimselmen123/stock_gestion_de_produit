package com.example.stock.dto.brand;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Brand response data.
 * Contains all brand information for detailed responses.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponseDTO {

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
     * The description of the brand.
     */
    private String description;

    /**
     * URL to the brand's image/logo.
     */
    private String imageUrl;

    /**
     * Timestamp when the brand was created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when the brand was last updated.
     */
    private LocalDateTime updatedAt;
}
