package com.example.stock.dto.inventoryitem;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object for creating a new InventoryItem.
 * Contains only the fields required for inventory item creation.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemCreateDTO {

    /**
     * The name of the inventory item.
     * Must be unique and between 2-100 characters.
     */
    @NotBlank(message = "Inventory item name is required")
    @Size(min = 2, max = 100, message = "Inventory item name must be between 2 and 100 characters")
    private String name;

    /**
     * The threshold quantity for low stock alerts.
     * Must be non-negative.
     */
    @NotNull(message = "Threshold quantity is required")
    @Min(value = 0, message = "Threshold quantity cannot be negative")
    private Integer thresholdQuantity;

    /**
     * The quantity to reorder when stock is low.
     * Must be greater than threshold quantity.
     */
    @NotNull(message = "Reorder quantity is required")
    @Min(value = 1, message = "Reorder quantity must be at least 1")
    private Integer reorderQuantity;

    /**
     * The unit purchase price of the item.
     * Must be positive with maximum 2 decimal places.
     */
    @NotNull(message = "Unit purchase price is required")
    @DecimalMin(value = "0.01", message = "Unit purchase price must be positive")
    @Digits(integer = 13, fraction = 2, message = "Unit purchase price cannot have more than 13 integer digits and 2 decimal places")
    private BigDecimal unitPurchasePrice;

    /**
     * The category ID this item belongs to.
     * Required field.
     */
    @NotBlank(message = "Category ID is required")
    private String categoryId;

    /**
     * The unit ID for this item.
     * Required field.
     */
    @NotBlank(message = "Unit ID is required")
    private String unitId;


}
