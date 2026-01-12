package com.example.stock.repository;

import com.example.stock.entity.MenuItemSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuItemSnapshotRepository
        extends JpaRepository<MenuItemSnapshot, Long> {

    Optional<MenuItemSnapshot> findByPosMenuItemId(Long posMenuItemId);
}