package com.example.stock.dto.tax;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for Tax filtering and search parameters
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxFilterDTO {

    private String branchId;
    private String name;
    private BigDecimal minRate;
    private BigDecimal maxRate;
    
    // Pagination parameters
    @Builder.Default
    private int page = 0;
    
    @Builder.Default
    private int size = 10;
    
    @Builder.Default
    private String sortBy = "name";
    
    @Builder.Default
    private String sortDir = "asc";
}