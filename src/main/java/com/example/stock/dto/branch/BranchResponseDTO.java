package com.example.stock.dto.branch;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Simplified Response DTO for Branch entity.
 * Contains only essential fields for frontend.
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

    @JsonProperty("description")
    private String description;

    @JsonProperty("created_at")
    @JsonAlias({"created", "createdAt"}) // Accepts both names
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    @JsonAlias({"updated", "updatedAt"}) // Accepts both names
    private LocalDateTime updatedAt;
}