package com.example.stock.controller;

import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.dto.inventorystock.InventoryStockResponseDTO;
import com.example.stock.dto.inventorystock.InventoryStockSummaryDTO;
import com.example.stock.service.InventoryStockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventoryStockController.class)
@SuppressWarnings("removal")
class InventoryStockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryStockService inventoryStockService;

    private InventoryStockResponseDTO responseDTO;
    private InventoryStockSummaryDTO summaryDTO;

    @BeforeEach
    void setup() {
        responseDTO = InventoryStockResponseDTO.builder()
                .id("s1")
                .inventoryItemId("item-1")
                .branchId("b1")
                .departmentId("d1")
                .currentQuantity(new BigDecimal("10.500000"))
                .averageUnitCost(new BigDecimal("2.000000"))
                .totalValue(new BigDecimal("21.000000"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        summaryDTO = InventoryStockSummaryDTO.builder()
                .id("s1")
                .inventoryItemId("item-1")
                .branchId("b1")
                .departmentId("d1")
                .currentQuantity(new BigDecimal("10.500000"))
                .averageUnitCost(new BigDecimal("2.000000"))
                .totalValue(new BigDecimal("21.000000"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void getCurrentStock_ShouldReturnPaginatedResponse_WithFilters() throws Exception {
        // Given
        PaginatedResponse<InventoryStockResponseDTO> page = PaginatedResponse.<InventoryStockResponseDTO>builder()
                .data(List.of(responseDTO))
                .totalElements(1L)
                .totalPages(1)
                .currentPage(0)
                .pageSize(20)
                .hasNext(false)
                .hasPrevious(false)
                .build();
        when(inventoryStockService.findAllWithFilters(any(InventoryStockService.Filters.class), eq(1), eq(20), eq("createdAt"), eq("desc")))
                .thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/stock/currentstock")
                        .param("search", "item-1")
                        .param("branch_id", "b1")
                        .param("department_id", "d1")
                        .param("inventory_item_id", "item-1")
                        .param("qty_min", "5")
                        .param("qty_max", "100")
                        .param("page", "1")
                        .param("per_page", "20")
                        .param("sort_field", "createdAt")
                        .param("sort_direction", "desc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Current stock retrieved successfully"))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.data[0].inventory_item_id").value("item-1"))
                .andExpect(jsonPath("$.data.data[0].branch_id").value("b1"))
                .andExpect(jsonPath("$.data.pageSize").value(20));

        ArgumentCaptor<InventoryStockService.Filters> filtersCaptor = ArgumentCaptor.forClass(InventoryStockService.Filters.class);
        verify(inventoryStockService).findAllWithFilters(filtersCaptor.capture(), eq(1), eq(20), eq("createdAt"), eq("desc"));
        InventoryStockService.Filters filters = filtersCaptor.getValue();
        assertThat(filters.search()).isEqualTo("item-1");
        assertThat(filters.branchId()).isEqualTo("b1");
        assertThat(filters.departmentId()).isEqualTo("d1");
        assertThat(filters.inventoryItemId()).isEqualTo("item-1");
        assertThat(filters.qtyMin()).isEqualByComparingTo("5");
        assertThat(filters.qtyMax()).isEqualByComparingTo("100");
    }

    @Test
    void getStockSummary_ShouldReturnPaginatedResponse_WithFilters() throws Exception {
        // Given
        PaginatedResponse<InventoryStockSummaryDTO> page = PaginatedResponse.<InventoryStockSummaryDTO>builder()
                .data(List.of(summaryDTO))
                .totalElements(1L)
                .totalPages(1)
                .currentPage(0)
                .pageSize(10)
                .hasNext(false)
                .hasPrevious(false)
                .build();
        when(inventoryStockService.findAllSummariesWithFilters(any(InventoryStockService.Filters.class), eq(2), eq(10), eq("createdAt"), eq("asc")))
                .thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/stock/summary")
                        .param("search", "item")
                        .param("branch_id", "b1")
                        .param("department_id", "d1")
                        .param("inventory_item_id", "item-1")
                        .param("qty_min", "1")
                        .param("qty_max", "50")
                        .param("page", "2")
                        .param("per_page", "10")
                        .param("sort_field", "createdAt")
                        .param("sort_direction", "asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Stock summary retrieved successfully"))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.data[0].branch_id").value("b1"))
                .andExpect(jsonPath("$.data.pageSize").value(10));

        ArgumentCaptor<InventoryStockService.Filters> filtersCaptor = ArgumentCaptor.forClass(InventoryStockService.Filters.class);
        verify(inventoryStockService).findAllSummariesWithFilters(filtersCaptor.capture(), eq(2), eq(10), eq("createdAt"), eq("asc"));
        InventoryStockService.Filters filters = filtersCaptor.getValue();
        assertThat(filters.search()).isEqualTo("item");
        assertThat(filters.branchId()).isEqualTo("b1");
        assertThat(filters.departmentId()).isEqualTo("d1");
        assertThat(filters.inventoryItemId()).isEqualTo("item-1");
        assertThat(filters.qtyMin()).isEqualByComparingTo("1");
        assertThat(filters.qtyMax()).isEqualByComparingTo("50");
    }
}
