package com.example.stock.service.impl;

import com.example.stock.service.MenuItemService;
import com.example.stock.dto.menuitemssnapshot.PosMenuItemDTO;
import com.example.stock.dto.sellableitem.SellableItemResponseDTO;
import com.example.stock.dto.variation.VariationDTO;
import com.example.stock.entity.MenuItemSnapshot;
import com.example.stock.entity.MenuItemVariationSnapshot;
import com.example.stock.entity.SellableItem;
import com.example.stock.mapper.MenuItemMapper;
import com.example.stock.mapper.MenuItemVariationMapper;
import com.example.stock.mapper.SellableItemMapper;
import com.example.stock.repository.MenuItemSnapshotRepository;
import com.example.stock.repository.MenuItemVariationSnapshotRepository;
import com.example.stock.repository.SellableItemRepository;
import com.example.stock.integration.PosStockClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MenuItemServiceImpl implements MenuItemService {

    private final PosStockClient posStockClient;
    private final MenuItemSnapshotRepository menuItemRepository;
    private final MenuItemVariationSnapshotRepository variationRepository;
    private final SellableItemRepository sellableItemRepository;
    private final MenuItemMapper menuItemMapper;
    private final MenuItemVariationMapper variationMapper;
    private final SellableItemMapper sellableItemMapper;

    @Override
    public void syncMenuItemsFromPos(String stockKey) {
        log.info("Starting synchronization of menu items from POS");
        
        try {
            List<PosMenuItemDTO> posItems = posStockClient.fetchMenuItems(stockKey);
            log.info("Fetched {} menu items from POS", posItems.size());

            for (PosMenuItemDTO posItem : posItems) {
                try {
                    syncMenuItem(posItem);
                } catch (Exception e) {
                    log.error("Error syncing menu item with POS ID: {}", posItem.getId(), e);
                    // Continue with next item instead of failing entire sync
                }
            }

            log.info("Synchronization completed successfully");
        } catch (Exception e) {
            log.error("Failed to synchronize menu items from POS", e);
            throw new RuntimeException("Failed to synchronize menu items", e);
        }
    }

    /**
     * Synchronize a single menu item: save/update menu item, variations, and sellable items.
     */
    private void syncMenuItem(PosMenuItemDTO posItem) {
        log.debug("Syncing menu item: {} (POS ID: {})", posItem.getItem_name(), posItem.getId());

        // Step 1: Save or update MenuItemSnapshot
        MenuItemSnapshot menuItem = saveOrUpdateMenuItem(posItem);

        // Step 2: Sync variations
        syncVariations(posItem, menuItem.getId());

        // Step 3: Create/update sellable items
        syncSellableItems(posItem, menuItem);
    }

    /**
     * Save or update a MenuItemSnapshot entity.
     */
    private MenuItemSnapshot saveOrUpdateMenuItem(PosMenuItemDTO posItem) {
        Optional<MenuItemSnapshot> existing = menuItemRepository
                .findByPosMenuItemId(posItem.getId());

        if (existing.isPresent()) {
            // UPDATE existing menu item
            MenuItemSnapshot entity = existing.get();
            entity.setName(posItem.getItem_name());
            entity.setImage(posItem.getImage());
            entity.setBranchId(posItem.getBranch_id());
            entity.setUpdatedAt(LocalDateTime.now());
            
            MenuItemSnapshot saved = menuItemRepository.save(entity);
            log.debug("Updated menu item: {} (ID: {})", saved.getName(), saved.getId());
            return saved;
        } else {
            // INSERT new menu item
            MenuItemSnapshot entity = menuItemMapper.toEntity(posItem);
            entity.setId(null); // Ensure new record
            entity.setCreatedAt(LocalDateTime.now());
            entity.setUpdatedAt(null);
            
            MenuItemSnapshot saved = menuItemRepository.save(entity);
            log.debug("Created menu item: {} (ID: {})", saved.getName(), saved.getId());
            return saved;
        }
    }

    /**
     * Synchronize variations for a menu item.
     */
    private void syncVariations(PosMenuItemDTO posItem, Long menuItemSnapshotId) {
        List<VariationDTO> posVariations = posItem.getVariations();
        
        if (posVariations == null || posVariations.isEmpty()) {
            log.debug("Menu item {} has no variations", menuItemSnapshotId);
            return;
        }

        log.debug("Syncing {} variations for menu item {}", posVariations.size(), menuItemSnapshotId);

        for (VariationDTO posVariation : posVariations) {
            saveOrUpdateVariation(posVariation, menuItemSnapshotId);
        }
    }

    /**
     * Save or update a MenuItemVariationSnapshot entity.
     */
    private void saveOrUpdateVariation(VariationDTO posVariation, Long menuItemSnapshotId) {
        Optional<MenuItemVariationSnapshot> existing = variationRepository
                .findById(posVariation.getId());

        if (existing.isPresent()) {
            // UPDATE existing variation
            MenuItemVariationSnapshot entity = existing.get();
            entity.setName(posVariation.getVariation());
            entity.setMenuItemSnapshotId(menuItemSnapshotId);
            
            variationRepository.save(entity);
            log.debug("Updated variation: {} (ID: {})", entity.getName(), entity.getId());
        } else {
            // INSERT new variation
            MenuItemVariationSnapshot entity = variationMapper.toEntity(posVariation);
            entity.setMenuItemSnapshotId(menuItemSnapshotId);
            
            variationRepository.save(entity);
            log.debug("Created variation: {} (ID: {})", entity.getName(), entity.getId());
        }
    }

    /**
     * Synchronize sellable items for a menu item according to business rules:
     * - If no variations → create 1 sellable item
     * - If has variations → create 1 sellable item per variation
     */
    private void syncSellableItems(PosMenuItemDTO posItem, MenuItemSnapshot menuItem) {
        List<VariationDTO> posVariations = posItem.getVariations();
        boolean hasVariations = posVariations != null && !posVariations.isEmpty();

        if (hasVariations) {
            // Rule: Create one sellable item per variation
            log.debug("Menu item {} has variations, creating sellable items for each variation", 
                    menuItem.getId());
            
            for (VariationDTO posVariation : posVariations) {
                saveOrUpdateSellableItemForVariation(menuItem, posVariation);
            }
        } else {
            // Rule: Create one sellable item without variation
            log.debug("Menu item {} has no variations, creating single sellable item", 
                    menuItem.getId());
            
            saveOrUpdateSellableItemWithoutVariation(menuItem);
        }
    }

    /**
     * Save or update a sellable item for a menu item with a variation.
     */
    private void saveOrUpdateSellableItemForVariation(
            MenuItemSnapshot menuItem, 
            VariationDTO posVariation) {
        
        Optional<SellableItem> existing = sellableItemRepository
                .findByMenuItemSnapshotIdAndMenuItemVariationSnapshotId(
                        menuItem.getId(), 
                        posVariation.getId());

        if (existing.isPresent()) {
            // UPDATE existing sellable item
            SellableItem entity = existing.get();
            entity.setName(posVariation.getVariation());
            
            sellableItemRepository.save(entity);
            log.debug("Updated sellable item for variation: {} (ID: {})", 
                    entity.getName(), entity.getId());
        } else {
            // INSERT new sellable item
            SellableItem entity = SellableItem.builder()
                    .menuItemSnapshotId(menuItem.getId())
                    .menuItemVariationSnapshotId(posVariation.getId())
                    .name(posVariation.getVariation())
                    .build();
            
            sellableItemRepository.save(entity);
            log.debug("Created sellable item for variation: {} (ID: {})", 
                    entity.getName(), entity.getId());
        }
    }

    /**
     * Save or update a sellable item for a menu item without variations.
     */
    private void saveOrUpdateSellableItemWithoutVariation(MenuItemSnapshot menuItem) {
        Optional<SellableItem> existing = sellableItemRepository
                .findByMenuItemSnapshotIdAndMenuItemVariationSnapshotIdIsNull(menuItem.getId());

        if (existing.isPresent()) {
            // UPDATE existing sellable item
            SellableItem entity = existing.get();
            entity.setName(menuItem.getName());
            
            sellableItemRepository.save(entity);
            log.debug("Updated sellable item without variation: {} (ID: {})", 
                    entity.getName(), entity.getId());
        } else {
            // INSERT new sellable item
            SellableItem entity = SellableItem.builder()
                    .menuItemSnapshotId(menuItem.getId())
                    .menuItemVariationSnapshotId(null) // NULL = no variation
                    .name(menuItem.getName())
                    .build();
            
            sellableItemRepository.save(entity);
            log.debug("Created sellable item without variation: {} (ID: {})", 
                    entity.getName(), entity.getId());
        }
    }

    @Override
    public List<SellableItemResponseDTO> getAllSellableItems() {
        log.debug("Fetching all sellable items");
        
        return sellableItemRepository.findAll()
                .stream()
                .map(sellableItemMapper::toResponseDTO)
                .toList();
    }
}