package com.example.stock.specification;

import com.example.stock.entity.InventoryStock;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class InventoryStockSpecificationsTest {

    @Test
    void build_ShouldComposeSpecifications_NonNulls() {
        Specification<InventoryStock> spec = InventoryStockSpecifications.build(
                "b1", "d1", "i1", "search", new BigDecimal("1"), new BigDecimal("10"));
        // We can only assert non-null; detailed behavior is verified in integration tests.
        assertThat(spec).isNotNull();
    }

    @Test
    void build_ShouldHandleNulls() {
        Specification<InventoryStock> spec = InventoryStockSpecifications.build(
                null, null, null, null, null, null);
        assertThat(spec).isNotNull();
    }
}
