package com.example.stock.dto.unit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for updating an existing Unit.
 * Contains only the fields that can be updated.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitUpdateDTO {

    /**
     * The updated name of the unit.
     * Must be between 2-50 characters.
     */
    @NotBlank(message = "Unit name is required")
    @Size(min = 2, max = 50, message = "Unit name must be between 2 and 50 characters")
    private String name;

    /**
     * The updated symbol of the unit.
     * Must be unique and between 1-10 characters.
     */
    @NotBlank(message = "Unit symbol is required")
    @Size(min = 1, max = 10, message = "Unit symbol must be between 1 and 10 characters")
    private String symbol;
}
