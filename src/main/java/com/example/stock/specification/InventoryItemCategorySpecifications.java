package com.example.stock.specification;

import com.example.stock.entity.InventoryItemCategory;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Specifications for InventoryItemCategory entity filtering.
 * Provides reusable and composable filter predicates for category queries.
 * 
 * @author Generated
 * @since 1.0
 */
public class InventoryItemCategorySpecifications {

    private InventoryItemCategorySpecifications() {
        // Utility class
    }

    /**
     * Creates a specification to filter categories by name containing the given text (case-insensitive).
     * 
     * @param name the name text to search for
     * @return specification that filters by name containing the text
     */
    public static Specification<InventoryItemCategory> hasNameContaining(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String pattern = "%" + name.toLowerCase() + "%";
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern);
        };
    }

    /**
     * Creates a specification to filter categories by branch ID.
     * 
     * @param branchId the branch ID to filter by
     * @return specification that filters by branch ID
     */
    public static Specification<InventoryItemCategory> hasBranchId(String branchId) {
        return (root, query, criteriaBuilder) -> {
            if (branchId == null || branchId.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("branchId"), branchId);
        };
    }

    /**
     * Creates a specification to filter categories by department ID.
     * 
     * @param departmentId the department ID to filter by
     * @return specification that filters by department ID
     */
    public static Specification<InventoryItemCategory> hasDepartmentId(String departmentId) {
        return (root, query, criteriaBuilder) -> {
            if (departmentId == null || departmentId.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("departmentId"), departmentId);
        };
    }

    /**
     * Creates a specification to filter categories by creation date range.
     * 
     * @param from the start date (inclusive)
     * @param to the end date (inclusive)
     * @return specification that filters by creation date range
     */
    public static Specification<InventoryItemCategory> hasCreationDateBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (from != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), from));
            }
            
            if (to != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), to));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Creates a specification to filter categories by update date range.
     * 
     * @param from the start date (inclusive)
     * @param to the end date (inclusive)
     * @return specification that filters by update date range
     */
    public static Specification<InventoryItemCategory> hasUpdateDateBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (from != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("updatedAt"), from));
            }
            
            if (to != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("updatedAt"), to));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Creates a specification for global search across multiple fields.
     * Searches in name field.
     * 
     * @param searchTerm the term to search for
     * @return specification that performs global search
     */
    public static Specification<InventoryItemCategory> globalSearch(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            
            String pattern = "%" + searchTerm.toLowerCase() + "%";
            
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern);
        };
    }

    /**
     * Creates a complex specification combining multiple filters.
     * 
     * @param search global search term
     * @param name name filter
     * @param branchId branch ID filter
     * @param departmentId department ID filter
     * @param createdFrom creation date from
     * @param createdTo creation date to
     * @param updatedFrom update date from
     * @param updatedTo update date to
     * @return combined specification with all filters
     */
    public static Specification<InventoryItemCategory> withFilters(
            String search, String name, String branchId, String departmentId,
            LocalDateTime createdFrom, LocalDateTime createdTo,
            LocalDateTime updatedFrom, LocalDateTime updatedTo) {
        
        return globalSearch(search)
                .and(hasNameContaining(name))
                .and(hasBranchId(branchId))
                .and(hasDepartmentId(departmentId))
                .and(hasCreationDateBetween(createdFrom, createdTo))
                .and(hasUpdateDateBetween(updatedFrom, updatedTo));
    }
}