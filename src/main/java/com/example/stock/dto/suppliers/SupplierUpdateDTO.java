package com.example.stock.dto.suppliers;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for updating an existing Supplier.
 * Contains all updatable fields for supplier modification.
 * Matches frontend documentation structure exactly.
 * 
 * @author Development Team
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request object for updating an existing supplier")
public class SupplierUpdateDTO {

    /**
     * Supplier name - required field.
     */
    @NotBlank(message = "Supplier name is required")
    @Size(max = 255, message = "Supplier name cannot exceed 255 characters")
    @Schema(description = "Supplier company name", example = "Coffee Supplier Co.")
    private String name;

    /**
     * Branch ID this supplier belongs to - required field.
     */
    @NotBlank(message = "Branch ID is required")
    @JsonProperty("branch_id")
    @Schema(description = "Branch UUID where supplier operates", example = "branch-uuid-123")
    private String branchId;

    /**
     * Supplier email - optional but must be valid format if provided.
     */
    @Email(message = "Email must be a valid email address")
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    @Schema(description = "Supplier contact email", example = "contact@coffeesupplier.com")
    private String email;

    /**
     * Supplier phone number - optional.
     */
    @Size(max = 20, message = "Phone cannot exceed 20 characters")
    @Schema(description = "Supplier contact phone", example = "+216 71 123 456")
    private String phone;

    /**
     * Supplier address - optional.
     */
    @Size(max = 500, message = "Address cannot exceed 500 characters")
    @Schema(description = "Supplier physical address", example = "123 Supplier Street, Tunis")
    private String address;

    /**
     * Supplier description - optional.
     */
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Schema(description = "Supplier business description", example = "Premium coffee supplier since 1990")
    private String description;

    /**
     * Extended business information - optional.
     */
    @Valid
    @JsonProperty("additional_info")
    @Schema(description = "Extended business information including finance, payment, contacts, operations, documents, and tax details")
    private SupplierAdditionalInfoDTO additionalInfo;
}
