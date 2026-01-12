package com.example.stock.repository;

import com.example.stock.entity.SellableItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellableItemRepository
        extends JpaRepository<SellableItem, Long> {

    List<SellableItem> findByMenuItemSnapshotId(Long menuItemSnapshotId);

    Optional<SellableItem> findByMenuItemSnapshotIdAndMenuItemVariationSnapshotId(
            Long menuItemSnapshotId, 
            Long menuItemVariationSnapshotId);

    Optional<SellableItem> findByMenuItemSnapshotIdAndMenuItemVariationSnapshotIdIsNull(
            Long menuItemSnapshotId);
    List<SellableItem> findByMenuItemVariationSnapshotId(Long menuItemVariationSnapshotId);

    boolean existsByMenuItemSnapshotId(Long menuItemSnapshotId);
}