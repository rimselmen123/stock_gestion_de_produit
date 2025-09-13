package com.example.stock.dto.unit;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Unit response data.
 * Contains all unit information for detailed responses.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnitResponseDTO {

    /**
     * The unique identifier of the unit.
     * UUID format string.
     */
    private String id;

    /**
     * The name of the unit.
     */
    private String name;

    /**
     * The symbol of the unit.
     */
    private String symbol;

    /**
     * The branch ID that this unit belongs to.
     */
    @JsonProperty("branch_id")
    private String branchId;

    /**
     * The department ID that this unit belongs to.
     */
    @JsonProperty("department_id")
    private String departmentId;

    /**
     * Embedded branch information (nullable if not fetched).
     */
    private BranchInfo branch;

    /**
     * Embedded department information (nullable if not fetched).
     */
    private DepartmentInfo department;

    /**
     * Timestamp when the unit was created.
     */
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    /**
     * Timestamp when the unit was last updated.
     */
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    /**
     * Lightweight embedded branch representation.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BranchInfo {
        private String id;
        private String name;
        private String description;
    }

    /**
     * Lightweight embedded department representation.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DepartmentInfo {
        private String id;
        private String name;
        private String description;
    }
}
