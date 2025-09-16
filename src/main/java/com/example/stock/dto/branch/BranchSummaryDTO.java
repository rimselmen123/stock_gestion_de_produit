package com.example.stock.dto.branch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simplified DTO for Branch entity.
 * Used for dropdowns, selects, and reference lists.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchSummaryDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("code")
    private String code;

    @JsonProperty("is_active")
    private Boolean isActive;

    /**
     * Nom complet pour affichage dans les dropdowns.
     */
    public String getDisplayName() {
        StringBuilder display = new StringBuilder();
        
        // Format: "CODE - Name (description)"
        if (code != null && !code.isBlank()) {
            display.append(code).append(" - ");
        }
        display.append(name);
        
        if (description != null && !description.isBlank()) {
            display.append(" (").append(description).append(")");
        }
        
        // Indiquer si inactive
        if (Boolean.FALSE.equals(isActive)) {
            display.append(" [Inactive]");
        }
        
        return display.toString();
    }

    /**
     * Nom court pour affichage compact.
     */
    public String getShortName() {
        return code != null && !code.isBlank() ? code : name;
    }
}
