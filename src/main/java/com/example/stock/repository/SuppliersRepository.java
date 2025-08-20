package com.example.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.stock.entity.Suppliers;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

@Repository
public interface SuppliersRepository extends JpaRepository<Suppliers, String>, JpaSpecificationExecutor<Suppliers> {

    /**
     * Find a supplier by email.
     */
    Optional<Suppliers> findByEmail(String email);

    /**
     * Check if a supplier exists by email.
     */
    boolean existsByEmail(String email);

    /**
     * Check if a supplier exists by name.
     */
    boolean existsByName(String name);

    /**
     * Find suppliers by name or email containing the specified text (case-insensitive) with pagination.
     */
    @Query("SELECT s FROM Suppliers s WHERE " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    Page<Suppliers> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
        @Param("name") String name,
        @Param("email") String email,
        Pageable pageable);
}
