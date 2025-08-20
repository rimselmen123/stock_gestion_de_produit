package com.example.stock.dto.suppliers;

import lombok.Builder;
import lombok.Data;
/**
 * Data Transfer Object for supliers summary information.
 * Contains minimal supliers data for listings and dropdowns.
 * Optimized for performance when full details are not needed.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@Builder
public class SupplierSummaryDTO {

    private String id;

    private String name;

    private String email;

    private String phone;
}