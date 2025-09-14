package com.example.stock.dto.suppliers;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a new Supplier.
 * Contains all required and optional fields for supplier creation.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierCreateDTO {

    /**
     * Supplier name - required field.
     */
    @NotBlank(message = "Supplier name is required")
    @Size(max = 255, message = "Supplier name must not exceed 255 characters")
    private String name;

    /**
     * Branch ID this supplier belongs to - required field.
     */
    @NotBlank(message = "Branch ID is required")
    @JsonProperty("branch_id")
    private String branchId;

    /**
     * Supplier email - optional but must be valid format if provided.
     */
    @Email(message = "Email must be a valid email address")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    /**
     * Supplier phone number - optional.
     */
    @Size(max = 50, message = "Phone must not exceed 50 characters")
    private String phone;

    /**
     * Supplier address - optional.
     */
    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    /**
     * Supplier description - optional.
     */
    private String description;

    /**
     * Additional information (JSON string) - optional.
     */
    @JsonProperty("additional_info")
    private String additionalInfo;
}
