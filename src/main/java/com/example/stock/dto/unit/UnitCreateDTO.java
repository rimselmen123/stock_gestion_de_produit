package com.example.stock.dto.unit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a new Unit.
 * Contains only the fields required for unit creation.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitCreateDTO {

    /**
     * The name of the unit.
     * Must not be blank and cannot exceed 255 characters.
     */
    @NotBlank(message = "Unit name is required")
    @Size(max = 255, message = "Unit name must not exceed 255 characters")
    private String name;

    /**
     * The symbol of the unit.
     * Must not be blank and cannot exceed 10 characters.
     */
    @NotBlank(message = "Unit symbol is required")
    @Size(max = 10, message = "Unit symbol must not exceed 10 characters")
    private String symbol;
}
