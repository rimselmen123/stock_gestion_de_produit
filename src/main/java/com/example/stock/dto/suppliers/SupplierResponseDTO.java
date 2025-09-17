package com.example.stock.dto.suppliers;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for supplier response data.
 * Contains all supplier information for detailed responses.
 * Matches frontend documentation structure exactly.
 * 
 * @author Development Team
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response object containing complete supplier information")
public class SupplierResponseDTO {

    /**
     * The unique identifier of the supplier.
     * UUID format string.
     */
    @Schema(description = "Unique supplier identifier", example = "supplier-uuid-123")
    private String id;

    /**
     * Supplier name.
     */
    @Schema(description = "Supplier company name", example = "Coffee Supplier Co.")
    private String name;

    /**
     * Branch ID this supplier belongs to.
     */
    @JsonProperty("branch_id")
    @Schema(description = "Branch UUID where supplier operates", example = "branch-uuid-123")
    private String branchId;

    /**
     * Supplier email address.
     */
    @Schema(description = "Supplier contact email", example = "contact@coffeesupplier.com")
    private String email;

    /**
     * Supplier phone number.
     */
    @Schema(description = "Supplier contact phone", example = "+216 71 123 456")
    private String phone;

    /**
     * Supplier address.
     */
    @Schema(description = "Supplier physical address", example = "123 Supplier Street, Tunis")
    private String address;

    /**
     * Supplier description.
     */
    @Schema(description = "Supplier business description", example = "Premium coffee supplier since 1990")
    private String description;

    /**
     * Extended business information.
     */
    @JsonProperty("additional_info")
    @Schema(description = "Extended business information including finance, payment, contacts, operations, documents, and tax details")
    private SupplierAdditionalInfoDTO additionalInfo;

    /**
     * Timestamp when the supplier was created.
     */
    @JsonProperty("created_at")
    @Schema(description = "Supplier creation timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    /**
     * Timestamp when the supplier was last updated.
     */
    @JsonProperty("updated_at")
    @Schema(description = "Supplier last update timestamp", example = "2024-01-20T14:45:00")
    private LocalDateTime updatedAt;
}
