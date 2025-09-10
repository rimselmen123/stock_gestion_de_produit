package com.example.stock.specification;

import com.example.stock.entity.Branch;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Specifications for Branch entity filtering.
 * Provides type-safe query building for complex filters.
 * 
 * @author Generated
 * @since 1.0
 */
public class BranchSpecifications {
    
    // Field name constants
    private static final String FIELD_NAME = "name";
    private static final String FIELD_LOCATION = "location";
    private static final String FIELD_CODE = "code";
    private static final String FIELD_IS_ACTIVE = "isActive";
    private static final String FIELD_CREATED_AT = "createdAt";
    
    // Private constructor to prevent instantiation
    private BranchSpecifications() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Create a comprehensive specification with all filters.
     */
    public static Specification<Branch> withFilters(
            String search,
            String name,
            String location,
            String code,
            Boolean isActive,
            LocalDateTime createdAfter,
            LocalDateTime createdBefore) {
        
        return (Root<Branch> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Global search filter
            if (StringUtils.hasText(search)) {
                String searchPattern = "%" + search.toLowerCase() + "%";
                Predicate searchPredicate = cb.or(
                    cb.like(cb.lower(root.get(FIELD_NAME)), searchPattern),
                    cb.like(cb.lower(cb.coalesce(root.get(FIELD_LOCATION), "")), searchPattern),
                    cb.like(cb.lower(cb.coalesce(root.get(FIELD_CODE), "")), searchPattern)
                );
                predicates.add(searchPredicate);
            }

            // Specific name filter
            if (StringUtils.hasText(name)) {
                String namePattern = "%" + name.toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get(FIELD_NAME)), namePattern));
            }

            // Specific location filter
            if (StringUtils.hasText(location)) {
                String locationPattern = "%" + location.toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(cb.coalesce(root.get(FIELD_LOCATION), "")), locationPattern));
            }

            // Specific code filter
            if (StringUtils.hasText(code)) {
                String codePattern = "%" + code.toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(cb.coalesce(root.get(FIELD_CODE), "")), codePattern));
            }

            // Active status filter
            if (isActive != null) {
                predicates.add(cb.equal(root.get(FIELD_IS_ACTIVE), isActive));
            }

            // Created after filter
            if (createdAfter != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get(FIELD_CREATED_AT), createdAfter));
            }

            // Created before filter
            if (createdBefore != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get(FIELD_CREATED_AT), createdBefore));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Filter by active status only.
     */
    public static Specification<Branch> isActive(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) {
                return cb.conjunction(); // No filter
            }
            return cb.equal(root.get("isActive"), active);
        };
    }

    /**
     * Filter by name containing (case-insensitive).
     */
    public static Specification<Branch> nameContains(String name) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(name)) {
                return cb.conjunction(); // No filter
            }
            String pattern = "%" + name.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("name")), pattern);
        };
    }

    /**
     * Filter by location containing (case-insensitive).
     */
    public static Specification<Branch> locationContains(String location) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(location)) {
                return cb.conjunction(); // No filter
            }
            String pattern = "%" + location.toLowerCase() + "%";
            return cb.like(cb.lower(cb.coalesce(root.get("location"), "")), pattern);
        };
    }

    /**
     * Filter by code containing (case-insensitive).
     */
    public static Specification<Branch> codeContains(String code) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(code)) {
                return cb.conjunction(); // No filter
            }
            String pattern = "%" + code.toLowerCase() + "%";
            return cb.like(cb.lower(cb.coalesce(root.get("code"), "")), pattern);
        };
    }

    /**
     * Filter by creation date range.
     */
    public static Specification<Branch> createdBetween(LocalDateTime after, LocalDateTime before) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (after != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), after));
            }
            
            if (before != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), before));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Global search across name, location, and code.
     */
    public static Specification<Branch> globalSearch(String search) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(search)) {
                return cb.conjunction(); // No filter
            }
            
            String pattern = "%" + search.toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get("name")), pattern),
                cb.like(cb.lower(cb.coalesce(root.get("location"), "")), pattern),
                cb.like(cb.lower(cb.coalesce(root.get("code"), "")), pattern)
            );
        };
    }
}
