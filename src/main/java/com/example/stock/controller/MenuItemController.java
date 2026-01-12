package com.example.stock.controller;

import com.example.stock.service.MenuItemService;
import com.example.stock.dto.sellableitem.SellableItemResponseDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.ResponseEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class MenuItemController {

    private final MenuItemService menuItemService;

    /**
     * Synchronize menu items, variations, and sellable items from POS system.
     * 
     * @param stockKey the authentication key for POS API
     * @return success message
     */
    @GetMapping("/sync")
    public ResponseEntity<String> syncMenuItems(
            @RequestHeader("stock-key") String stockKey) {
        log.info("Received sync request");
        
        try {
            menuItemService.syncMenuItemsFromPos(stockKey);
            log.info("Sync completed successfully");
            return ResponseEntity.ok("Synchronization completed successfully");
        } catch (Exception e) {
            log.error("Sync failed", e);
            return ResponseEntity.internalServerError()
                    .body("Synchronization failed: " + e.getMessage());
        }
    }

    /**
     * Get all sellable items (items that can be sold/ordered).
     * 
     * @return List of sellable items
     */
    @GetMapping
    public ResponseEntity<List<SellableItemResponseDTO>> getSellableItems() {
        log.debug("Fetching all sellable items");
        
        List<SellableItemResponseDTO> sellableItems = menuItemService.getAllSellableItems();
        log.debug("Returning {} sellable items", sellableItems.size());
        return ResponseEntity.ok(sellableItems);
    }
}