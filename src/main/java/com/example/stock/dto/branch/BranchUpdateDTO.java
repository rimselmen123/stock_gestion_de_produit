package com.example.stock.dto.branch;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating existing Branch entities.
 * All fields are optional for partial updates.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchUpdateDTO {

    @JsonProperty("name")
    @Size(min = 2, max = 100, message = "Branch name must be between 2 and 100 characters")
    private String name;

    @JsonProperty("description")
    @Size(max = 255, message = "description must not exceed 255 characters")
    private String description;

    @JsonProperty("code")
    @Size(min = 2, max = 20, message = "Branch code must be between 2 and 20 characters")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Branch code must contain only uppercase letters and numbers")
    private String code;

    @JsonProperty("is_active")
    private Boolean isActive;
}