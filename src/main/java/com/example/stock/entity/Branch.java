package com.example.stock.entity;

/* import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
//import java.util.List;
// hethi  l creation mta3 l branche taw nchofoha 
@Entity
@Table(name = "branch")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Branch {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "location")
    private String location; // facultatif, adresse ou ville

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
/* 
    // Relations
    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    private List<InventoryItemCategory> categories;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    private List<InventoryMovement> movements; }*/
