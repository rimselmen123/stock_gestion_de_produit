package com.example.stock.specification;

import com.example.stock.entity.InventoryItem;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * Utility class containing static methods to create Specifications for InventoryItem entities.
 * These specifications are used to build dynamic queries with JPA Criteria API.
 * 
 * Each method creates a Specification that can be combined with others to build complex queries.
 * If the input parameter is null or empty, the specification will be neutral (not filter anything).
 */
public class InventoryItemSpecifications {

    // Private constructor to prevent instantiation
    private InventoryItemSpecifications() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Creates a specification to filter items by name (partial match, case-insensitive).
     * 
     * @param name The name or part of the name to search for
     * @return Specification for filtering by name
     */
    public static Specification<InventoryItem> withName(String name) {
        return (root, query, criteriaBuilder) -> 
            // If name is not provided, return a neutral specification (no filtering)
            !StringUtils.hasText(name) ? 
                criteriaBuilder.conjunction() : 
                // Create a LIKE predicate for case-insensitive search
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")), 
                    "%" + name.trim().toLowerCase() + "%"
                );
    }

    /**
     * Creates a specification to filter items by branch ID (exact match).
     * 
     * @param branchId The ID of the branch to filter by
     * @return Specification for filtering by branch ID
     */
    public static Specification<InventoryItem> withBranchId(String branchId) {
        return (root, query, criteriaBuilder) -> 
            !StringUtils.hasText(branchId) ? 
                criteriaBuilder.conjunction() : 
                // Create an equality predicate for the branch ID
                criteriaBuilder.equal(root.get("branchId"), branchId.trim());
    }

    /**
     * Creates a specification to filter items by department ID (exact match).
     * 
     * @param departmentId The ID of the department to filter by
     * @return Specification for filtering by department ID
     */
    public static Specification<InventoryItem> withDepartmentId(String departmentId) {
        return (root, query, criteriaBuilder) -> 
            !StringUtils.hasText(departmentId) ? 
                criteriaBuilder.conjunction() : 
                // Create an equality predicate for the department ID
                criteriaBuilder.equal(root.get("departmentId"), departmentId.trim());
    }

    /**
     * Creates a specification to filter items by unit ID (exact match).
     * 
     * @param unitId The ID of the unit to filter by
     * @return Specification for filtering by unit ID
     */
    public static Specification<InventoryItem> withUnitId(String unitId) {
        return (root, query, criteriaBuilder) -> 
            !StringUtils.hasText(unitId) ? 
                criteriaBuilder.conjunction() : 
                // Create an equality predicate for the unit ID
                criteriaBuilder.equal(root.get("unit").get("id"), unitId.trim());
    }

    /**
     * Creates a specification to filter items by category ID (exact match).
     * 
     * @param categoryId The ID of the category to filter by
     * @return Specification for filtering by category ID
     */
    public static Specification<InventoryItem> withCategoryId(String categoryId) {
        return (root, query, criteriaBuilder) -> 
            !StringUtils.hasText(categoryId) ? 
                criteriaBuilder.conjunction() : 
                // Create an equality predicate for the category ID
                criteriaBuilder.equal(root.get("category").get("id"), categoryId.trim());
    }

    /**
     * Creates a specification to filter items by category name (partial match, case-insensitive).
     * 
     * @param categoryName The name or part of the category name to search for
     * @return Specification for filtering by category name
     */
    public static Specification<InventoryItem> withCategoryName(String categoryName) {
        return (root, query, criteriaBuilder) -> 
            !StringUtils.hasText(categoryName) ? 
                criteriaBuilder.conjunction() : 
                // Create a LIKE predicate for case-insensitive search on category name
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("category").get("name")), 
                    "%" + categoryName.trim().toLowerCase() + "%"
                );
    }

    /**
     * Creates a specification to filter items by unit name (partial match, case-insensitive).
     * 
     * @param unitName The name or part of the unit name to search for
     * @return Specification for filtering by unit name
     */
    public static Specification<InventoryItem> withUnitName(String unitName) {
        return (root, query, criteriaBuilder) -> 
            !StringUtils.hasText(unitName) ? 
                criteriaBuilder.conjunction() : 
                // Create a LIKE predicate for case-insensitive search on unit name
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("unit").get("name")), 
                    "%" + unitName.trim().toLowerCase() + "%"
                );
    }
}
