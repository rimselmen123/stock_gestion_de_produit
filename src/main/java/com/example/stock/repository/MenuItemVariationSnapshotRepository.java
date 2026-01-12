package com.example.stock.repository;

import com.example.stock.entity.MenuItemVariationSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository 
public interface MenuItemVariationSnapshotRepository extends JpaRepository<MenuItemVariationSnapshot, Long> {

    List<MenuItemVariationSnapshot> findByMenuItemSnapshotId(Long menuItemSnapshotId);

    boolean existsByMenuItemSnapshotId(Long menuItemSnapshotId);
}