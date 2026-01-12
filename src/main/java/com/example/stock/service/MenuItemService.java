package com.example.stock.service;

import com.example.stock.dto.sellableitem.SellableItemResponseDTO;
import java.util.List;

public interface MenuItemService {

    void syncMenuItemsFromPos(String stockKey);

    List<SellableItemResponseDTO> getAllSellableItems();
}
