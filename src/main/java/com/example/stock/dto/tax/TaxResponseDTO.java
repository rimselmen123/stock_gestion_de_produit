package com.example.stock.dto.tax;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Tax response data.
 * Contains all tax information for detailed responses.
 * Aligned with Tax entity and TaxMapper.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxResponseDTO {

    /**
     * The unique identifier of the tax.
     */
    @JsonProperty("id")
    private Long id;

    /**
     * The branch ID this tax belongs to.
     */
    @JsonProperty("branch_id")
    private String branchId;

    /**
     * The name of the tax.
     */
    @JsonProperty("name")
    private String name;

    /**
     * The tax rate percentage.
     */
    @JsonProperty("rate")
    private BigDecimal rate;

    /**
     * The description of the tax.
     */
    @JsonProperty("description")
    private String description;

    /**
     * Timestamp when the tax was created.
     */
    @JsonProperty("created_at")
    @JsonAlias({"created", "createdAt"})
    private LocalDateTime createdAt;

    /**
     * Timestamp when the tax was last updated.
     */
    @JsonProperty("updated_at")
    @JsonAlias({"updated", "updatedAt"})
    private LocalDateTime updatedAt;

    /**
     * Branch name for display (populated by mapper).
     */
    @JsonProperty("branch_name")
    private String branchName;

    /**
     * Display name for UI (populated by mapper).
     */
    @JsonProperty("display_name")
    private String displayName;

    /**
     * Embedded branch information (nullable if not fetched).
     */
    private BranchInfo branch;

    /**
     * Computed formatted rate for display (e.g., "15.5%").
     */
    @JsonProperty("formatted_rate")
    public String getFormattedRate() {
        return rate != null ? rate.toString() + "%" : "0%";
    }

    /**
     * Lightweight embedded branch representation.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BranchInfo {
        @JsonProperty("id")
        private String id;
        @JsonProperty("name")
        private String name;
        @JsonProperty("description")
        private String description;
    }
}
