package com.example.stock.dto.tax;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for Tax creation and update requests
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxRequestDTO {

    @JsonProperty("branchId")
    @NotBlank(message = "Branch ID is required")
    @Size(min = 1, max = 50, message = "Branch ID must be between 1 and 50 characters")
    private String branchId;

    @JsonProperty("name")
    @NotBlank(message = "Tax name is required")
    @Size(min = 2, max = 100, message = "Tax name must be between 2 and 100 characters")
    private String name;

    @JsonProperty("rate")
    @NotNull(message = "Tax rate is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Tax rate must be non-negative")
    @DecimalMax(value = "100.0", inclusive = true, message = "Tax rate cannot exceed 100%")
    @Digits(integer = 3, fraction = 2, message = "Tax rate must have at most 3 integer digits and 2 decimal places")
    private BigDecimal rate;

    @JsonProperty("description")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
}