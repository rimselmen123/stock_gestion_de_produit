    package com.example.stock.entity;

    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.Id;
    import jakarta.persistence.Table;
    import jakarta.persistence.OneToMany;
    import jakarta.persistence.CascadeType;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import jakarta.validation.constraints.Email;
    import jakarta.validation.constraints.NotBlank;
    import java.time.LocalDateTime;
    import java.util.List;

    @Entity
    @Table(name = "suppliers")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Suppliers {

        @Id
        private String id;

        // Required
        @NotBlank
        @Column(name = "name", nullable = false)
        private String name;

        // Optional, but must be a valid email format if provided
        @Email
        @Column(name = "email")
        private String email;

        // Optional phone
        @Column(name = "phone")
        private String phone;

        // Optional address
        @Column(name = "address")
        private String address;

        // Optional description
        @Column(name = "description")
        private String description;

        @Column(name = "created_at", nullable = false)
        private LocalDateTime createdAt;

        @Column(name = "updated_at", nullable = false)
        private LocalDateTime updatedAt;

        // Relations
        @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
        private List<InventoryMovement> movements;
    }
