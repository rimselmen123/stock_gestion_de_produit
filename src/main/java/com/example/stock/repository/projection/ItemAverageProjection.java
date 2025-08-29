package com.example.stock.repository.projection;

import java.math.BigDecimal;

public interface ItemAverageProjection {
    String getInventoryItemId();
    String getName();
    BigDecimal getMoyenne();
}
