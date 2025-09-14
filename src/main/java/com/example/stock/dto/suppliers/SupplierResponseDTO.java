package com.example.stock.dto.suppliers;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for supplier response data.
 * Contains all supplier information for detailed responses.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierResponseDTO {

    /**
     * The unique identifier of the supplier.
     * UUID format string.
     */
    private String id;

    /**
     * Supplier name.
     */
    private String name;

    /**
     * Branch ID this supplier belongs to.
     */
    @JsonProperty("branch_id")
    private String branchId;

    /**
     * Supplier email address.
     */
    private String email;

    /**
     * Supplier phone number.
     */
    private String phone;

    /**
     * Supplier address.
     */
    private String address;

    /**
     * Supplier description.
     */
    private String description;

    /**
     * Additional information (JSON string).
     */
    @JsonProperty("additional_info")
    private String additionalInfo;

    /**
     * Timestamp when the supplier was created.
     */
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    /**
     * Timestamp when the supplier was last updated.
     */
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
