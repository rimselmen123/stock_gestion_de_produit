package com.example.stock.dto.branch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for Branch entity.
 * Used for API responses and data transfer.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchResponseDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("location")
    private String location;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    // Compteurs optionnels pour tableaux
    @JsonProperty("departments_count")
    private Long departmentsCount;

    @JsonProperty("movements_count")
    private Long movementsCount;
}