package com.example.stock.dto.suppliers;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for supliers response data.
 * Contains all supliers information for detailed responses.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierResponseDTO {
    //reponse de dto 
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String description;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
