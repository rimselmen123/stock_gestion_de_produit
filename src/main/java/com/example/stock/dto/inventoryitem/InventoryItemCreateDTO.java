package com.example.stock.dto.inventoryitem;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItemCreateDTO {

    @JsonProperty("name")
    @NotBlank(message = "Inventory item name is required")
    @Size(max = 255, message = "Inventory item name must not exceed 255 characters")
    private String name;

    @JsonProperty("branch_id")
    @NotBlank(message = "branch_id is required")
    private String branchId;

    @JsonProperty("department_id")
    @NotBlank(message = "department_id is required")
    private String departmentId;

    @JsonProperty("inventory_item_category_id")
    @NotBlank(message = "Category ID is required")
    private String inventoryItemCategoryId;

    @JsonProperty("unit_id")
    @NotBlank(message = "Unit ID is required")
    private String unitId;

    @JsonProperty("threshold_quantity")
    @NotNull(message = "Threshold quantity is required")
    @Positive(message = "Threshold quantity must be positive")
    private Integer thresholdQuantity;

    @JsonProperty("reorder_quantity")
    @NotNull(message = "Reorder quantity is required")
    @Positive(message = "Reorder quantity must be positive")
    private Integer reorderQuantity;

    @JsonProperty("tax_id")
    @NotBlank(message = "Tax ID is required")
    private String taxId;
}