package com.example.stock.dto.unit;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
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
     * Timestamp when the unit was created.
     */
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    /**
     * Timestamp when the unit was last updated.
     */
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
