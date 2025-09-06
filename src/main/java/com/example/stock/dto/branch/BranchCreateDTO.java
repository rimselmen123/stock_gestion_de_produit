package com.example.stock.dto.branch;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating new Branch entities.
 * Contains validation rules for branch creation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchCreateDTO {

    @JsonProperty("name")
    @NotBlank(message = "Branch name is required")
    @Size(min = 2, max = 100, message = "Branch name must be between 2 and 100 characters")
    private String name;

    @JsonProperty("location")
    @Size(max = 255, message = "Location must not exceed 255 characters")
    private String location;
}