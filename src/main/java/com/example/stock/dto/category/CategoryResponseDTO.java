package com.example.stock.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for InventoryItemCategory response data.
 * Contains all category information for detailed responses.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDTO {

    /**
     * The unique identifier of the category.
     * UUID format string.
     */
    private String id;

    /**
     * The name of the category.
     */
    private String name;

    /**
     * The branch ID this category belongs to.
     */
    @JsonProperty("branch_id")
    private String branchId;

    /**
     * Timestamp when the category was created.
     */
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    /**
     * Timestamp when the category was last updated.
     */
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
