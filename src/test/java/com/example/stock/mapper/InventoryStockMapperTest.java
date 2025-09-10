package com.example.stock.mapper;

import com.example.stock.dto.inventorystock.InventoryStockResponseDTO;
import com.example.stock.dto.inventorystock.InventoryStockSummaryDTO;
import com.example.stock.entity.InventoryStock;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class InventoryStockMapperTest {

    private final InventoryStockMapper mapper = Mappers.getMapper(InventoryStockMapper.class);

    @Test
    void toResponseDTO_ShouldMapFieldsCorrectly() {
        InventoryStock stock = InventoryStock.builder()
                .id("s1")
                .inventoryItemId("item-1")
                .branchId("b1")
                .departmentId("d1")
                .currentQuantity(new BigDecimal("10.500000"))
                .averageUnitCost(new BigDecimal("2.000000"))
                .totalValue(new BigDecimal("21.000000"))
                .lastMovementDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        InventoryStockResponseDTO dto = mapper.toResponseDTO(stock);
        assertThat(dto.getId()).isEqualTo("s1");
        assertThat(dto.getInventoryItemId()).isEqualTo("item-1");
        assertThat(dto.getBranchId()).isEqualTo("b1");
        assertThat(dto.getDepartmentId()).isEqualTo("d1");
        assertThat(dto.getCurrentQuantity()).isEqualByComparingTo("10.500000");
        assertThat(dto.getAverageUnitCost()).isEqualByComparingTo("2.000000");
        assertThat(dto.getTotalValue()).isEqualByComparingTo("21.000000");
        assertThat(dto.getInventoryItem()).isNull(); // ignored mapping
    }

    @Test
    void toSummaryDTO_ShouldMapFieldsCorrectly() {
        InventoryStock stock = InventoryStock.builder()
                .id("s1")
                .inventoryItemId("item-1")
                .branchId("b1")
                .departmentId("d1")
                .currentQuantity(new BigDecimal("10.500000"))
                .averageUnitCost(new BigDecimal("2.000000"))
                .totalValue(new BigDecimal("21.000000"))
                .lastMovementDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        InventoryStockSummaryDTO dto = mapper.toSummaryDTO(stock);
        assertThat(dto.getId()).isEqualTo("s1");
        assertThat(dto.getInventoryItemId()).isEqualTo("item-1");
        assertThat(dto.getBranchId()).isEqualTo("b1");
        assertThat(dto.getDepartmentId()).isEqualTo("d1");
        assertThat(dto.getCurrentQuantity()).isEqualByComparingTo("10.500000");
        assertThat(dto.getAverageUnitCost()).isEqualByComparingTo("2.000000");
        assertThat(dto.getTotalValue()).isEqualByComparingTo("21.000000");
        assertThat(dto.getInventoryItem()).isNull();
    }
}
