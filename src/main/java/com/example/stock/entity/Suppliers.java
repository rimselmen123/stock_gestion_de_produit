    package com.example.stock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Suppliers entity representing supplier information in the system.
 * Contains supplier details, contact information, and branch relations.
 * 
 * @author Generated
 * @since 1.0
 */
@Entity
@Table(name = "suppliers", 
       indexes = {
           @Index(name = "idx_suppliers_branch_id", columnList = "branch_id"),
           @Index(name = "idx_suppliers_name", columnList = "name"),
           @Index(name = "idx_suppliers_email", columnList = "email")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Suppliers {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    /**
     * Supplier name - required field.
     */
    @NotBlank(message = "Supplier name is required")
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /**
     * Branch ID this supplier belongs to - required field.
     */
    @NotBlank(message = "Branch ID is required")
    @Column(name = "branch_id", nullable = false)
    private String branchId;

    /**
     * Relation to Branch entity (read-only).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", insertable = false, updatable = false)
    private Branch branch;

    /**
     * Supplier email - optional but must be valid format if provided.
     */
    @Email(message = "Email must be a valid email address")
    @Column(name = "email", length = 255)
    private String email;

    /**
     * Supplier phone number - optional.
     */
    @Column(name = "phone", length = 50)
    private String phone;

    /**
     * Supplier address - optional.
     */
    @Column(name = "address", length = 500)
    private String address;

    /**
     * Supplier description - optional.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Additional information stored as JSON.
     */
    @Column(name = "additional_info", columnDefinition = "TEXT")
    private String additionalInfo;

    /**
     * Timestamp when the supplier was created.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the supplier was last updated.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Automatically update the updatedAt field before entity update.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * One-to-many relation with InventoryMovement.
     */
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InventoryMovement> movements;
    }
