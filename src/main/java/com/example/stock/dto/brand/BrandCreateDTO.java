package com.example.stock.dto.brand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a new Brand.
 * Contains only the fields required for brand creation.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandCreateDTO {

    /**
     * The name of the brand.
     * Must be unique and between 2-100 characters.
     */
    @NotBlank(message = "Brand name is required")
    @Size(min = 2, max = 100, message = "Brand name must be between 2 and 100 characters")
    private String name;

    /**
     * Optional description of the brand.
     * Maximum 500 characters.
     */
    @Size(max = 500, message = "Brand description cannot exceed 500 characters")
    private String description;

    /**
     * Optional URL to the brand's image/logo.
     * Maximum 255 characters.
     */
    @Size(max = 255, message = "Image URL cannot exceed 255 characters")
    private String imageUrl;
}
