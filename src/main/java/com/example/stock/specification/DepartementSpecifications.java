package com.example.stock.specification;

import com.example.stock.entity.Department;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

/**
 * Specifications utilitaires pour construire dynamiquement des requêtes Department.
 * (Nom de classe conservé tel quel pour éviter rupture, même si l'orthographe idéale serait DepartmentSpecifications.)
 */
public final class DepartementSpecifications {

	private DepartementSpecifications() {}

	/**
	 * Filtre contenant (case-insensitive) sur le champ name.
	 */
	public static Specification<Department> nameContains(String name) {
		if (name == null || name.isBlank()) return null; // Spring Data ignore null => pas de filtre
		String like = "%" + name.toLowerCase().trim() + "%";
		return (root, query, cb) -> cb.like(cb.lower(root.get("name")), like);
	}

	/**
	 * Filtre égalité sur branchId.
	 */
	public static Specification<Department> branchIdEquals(String branchId) {
		if (branchId == null || branchId.isBlank()) return null;
		return (root, query, cb) -> cb.equal(root.get(FIELD_BRANCH_ID), branchId.trim());
	}

	private static final String FIELD_DESCRIPTION = "description";
	private static final String FIELD_CREATED_AT = "createdAt";
	private static final String FIELD_BRANCH_ID = "branchId";

	/**
	 * Filtre plage de dates sur createdAt (bornes inclusives si fournies).
	 */
	public static Specification<Department> createdAtBetween(LocalDateTime from, LocalDateTime to) {
		if (from == null && to == null) return null;
		return (root, query, cb) -> {
			if (from != null && to != null) {
				return cb.between(root.get(FIELD_CREATED_AT), from, to);
			} else if (from != null) {
				return cb.greaterThanOrEqualTo(root.get(FIELD_CREATED_AT), from);
			} else { // to != null
				return cb.lessThanOrEqualTo(root.get(FIELD_CREATED_AT), to);
			}
		};
	}

	/**
	 * Filtre global search: teste name OU description.
	 */
	public static Specification<Department> globalSearch(String term) {
		if (term == null || term.isBlank()) return null;
		String like = "%" + term.toLowerCase().trim() + "%";
		return (root, query, cb) -> cb.or(
			cb.like(cb.lower(root.get("name")), like),
			cb.like(cb.lower(cb.coalesce(root.get(FIELD_DESCRIPTION), "")), like)
		);
	}

	/**
	 * (Optionnel futur) Filtre isActive – désactivé car champ absent dans entity actuellement.
	 * Dé-commentez si vous ajoutez le champ Boolean isActive dans Department.
	 */

	/**
	 * Builder combinant les filtres standards.
	 */
	public static Specification<Department> build(String search,
												  String name,
												  String branchId,
												  LocalDateTime createdFrom,
												  LocalDateTime createdTo) {
	Specification<Department> spec = null;
	spec = and(spec, globalSearch(search));
	spec = and(spec, nameContains(name));
	spec = and(spec, branchIdEquals(branchId));
	spec = and(spec, createdAtBetween(createdFrom, createdTo));
		// isActive ignoré (champ absent)
	return spec;
	}

	private static Specification<Department> and(Specification<Department> base, Specification<Department> other) {
		if (other == null) return base;
		return base == null ? other : base.and(other);
	}
}
