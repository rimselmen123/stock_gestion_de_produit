package com.example.stock.dto.inventorystock;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
//import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;

/**
 * Complete DTO for inventory stock responses, aligned with frontend contract.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryStockResponseDTO {

    private String id;

    @JsonProperty("inventory_item_id")
    private String inventoryItemId;

    @JsonProperty("branch_id")
    private String branchId;
    
    @JsonProperty("quantity")
    private BigDecimal quantity;

    @JsonProperty("moy")
    private BigDecimal moy;

    @JsonProperty("expiration_date")
    private LocalDate expirationDate;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    // hethom lel calcule mtack stock wel alert w jaw heka lkol 
    // /**
    //  * Stock status (NORMAL, ALERTE_BASSE, RUPTURE)
    //  */
    // @JsonProperty("stock_status")
    // private String stockStatus;

    /**
     * Embedded inventory item information.
     */
    @JsonProperty("inventory_item")
    private InventoryItemEmbeddedDTO inventoryItem;

    /**
     * Nested DTO for embedded inventory item information.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InventoryItemEmbeddedDTO {
        private String id;
        private String name;
        
        @JsonProperty("threshold_quantity")
        private Integer thresholdQuantity;
    }

    /**
     * DTO representing the weighted average purchase price per item.
     */
    /* 
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MoyenneParItemDTO {
        private String id;   // inventory item id
        private String name; // inventory item name
        private BigDecimal moyenne; // weighted average price
    }
        
    */
    /**
     * Compute weighted average unit purchase price per item.
     * moyenne = sum(price_i * qty_i) / sum(qty_i)
     * Items with null/zero quantity or null price are ignored.
     */
    /* 
    public static List<MoyenneParItemDTO> computeMoyenneParItem(List<InventoryStockResponseDTO> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        class Acc {
            BigDecimal sumPriceQty = BigDecimal.ZERO;
            BigDecimal sumQty = BigDecimal.ZERO;
            String name;
        }

        Map<String, Acc> accMap = new HashMap<>();

        for (InventoryStockResponseDTO dto : list) {
            if (dto == null) continue;
            String itemId = dto.getInventoryItemId();
            BigDecimal qty = dto.getQuantity();
            BigDecimal price = dto.getUnitPurchasePrice();

            if (itemId == null || qty == null || price == null) continue;
            if (BigDecimal.ZERO.compareTo(qty) == 0) continue;

            Acc acc = accMap.computeIfAbsent(itemId, k -> new Acc());
            acc.sumQty = acc.sumQty.add(qty);
            acc.sumPriceQty = acc.sumPriceQty.add(price.multiply(qty));
            if (dto.getInventoryItem() != null && !Objects.toString(dto.getInventoryItem().getName(), "").isEmpty()) {
                acc.name = dto.getInventoryItem().getName();
            }
        }

        List<MoyenneParItemDTO> result = new ArrayList<>();
        for (Map.Entry<String, Acc> e : accMap.entrySet()) {
            String itemId = e.getKey();
            Acc acc = e.getValue();
            if (acc.sumQty.compareTo(BigDecimal.ZERO) == 0) continue;
            BigDecimal avg = acc.sumPriceQty.divide(acc.sumQty, 4, RoundingMode.HALF_UP);
            result.add(MoyenneParItemDTO.builder()
                    .id(itemId)
                    .name(acc.name)
                    .moyenne(avg)
                    .build());
        }

        return result;
    }*/
}
