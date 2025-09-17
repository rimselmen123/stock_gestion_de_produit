package com.example.stock.service.impl;

import com.example.stock.dto.common.PaginatedResponse;
import com.example.stock.dto.common.PaginationInfo;
import com.example.stock.dto.suppliers.SupplierAdditionalInfoDTO;
import com.example.stock.dto.suppliers.SupplierCreateDTO;
import com.example.stock.dto.suppliers.SupplierResponseDTO;
import com.example.stock.dto.suppliers.SupplierUpdateDTO;
import com.example.stock.entity.Suppliers;
import com.example.stock.exception.DeleteConstraintException;
import com.example.stock.exception.ResourceNotFoundException;
import com.example.stock.mapper.SuppliersMapper;
import com.example.stock.repository.SuppliersRepository;
import com.example.stock.service.SuppliersService;
import com.example.stock.validation.SupplierAdditionalInfoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of SuppliersService interface.
 * Handles all business logic for Suppliers entity operations.
 * 
 * @author Generated
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SuppliersServiceImp implements SuppliersService {

    private static final String CREATED_AT_FIELD = "createdAt";
    private static final String SUPPLIER_ENTITY = "Supplier";

    private final SuppliersRepository suppliersRepository;
    private final SuppliersMapper suppliersMapper;
    private final SupplierAdditionalInfoValidator additionalInfoValidator;

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<SupplierResponseDTO> findAllWithPagination(
            String search, int page, int perPage, String sortField, String sortDirection) {
        
        log.debug("Finding suppliers with pagination - search: {}, page: {}, perPage: {}, sortField: {}, sortDirection: {}",
                search, page, perPage, sortField, sortDirection);

        // Validate and adjust pagination parameters
        page = Math.max(1, page);
        perPage = Math.min(Math.max(1, perPage), 100);

        // Create sort object
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, mapSortField(sortField));

        Pageable pageable = PageRequest.of(page - 1, perPage, sort);

        Page<Suppliers> supplierPage;
        if (search != null && !search.trim().isEmpty()) {
            supplierPage = suppliersRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                    search.trim(), search.trim(), pageable);
        } else {
            supplierPage = suppliersRepository.findAll(pageable);
        }

        List<SupplierResponseDTO> supplierDTOs = suppliersMapper.toResponseDTOList(supplierPage.getContent());

        // Create pagination info with proper current_page calculation
        int currentPage = supplierDTOs.isEmpty() ? 0 : page;
        int totalPages = supplierPage.getTotalPages();
        if (totalPages == 0) totalPages = 1;
        
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPage(currentPage);
        paginationInfo.setPerPage(perPage);
        paginationInfo.setTotal((int) supplierPage.getTotalElements());
        paginationInfo.setLastPage(totalPages);
        
        return PaginatedResponse.of(supplierDTOs, paginationInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<SupplierResponseDTO> findAllWithFilters(
            String branchId,
            String search,
            String name,
            String email,
            String phone,
            String address,
            String description,
            String createdFrom,
            String createdTo,
            String updatedFrom,
            String updatedTo,
            int page,
            int perPage,
            String sortField,
            String sortDirection) {

        log.debug("Finding suppliers with filters - branchId: {}, search: {}, name: {}, email: {}, phone: {}, address: {}, description: {}, createdFrom: {}, createdTo: {}, updatedFrom: {}, updatedTo: {}, page: {}, perPage: {}",
                branchId, search, name, email, phone, address, description, createdFrom, createdTo, updatedFrom, updatedTo, page, perPage);

        // Validate and adjust pagination parameters
        page = Math.max(1, page);
        perPage = Math.min(Math.max(1, perPage), 100);

        // Create sort object
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, mapSortField(sortField));

        Pageable pageable = PageRequest.of(page - 1, perPage, sort);

        Specification<Suppliers> spec = buildSpecification(branchId, search, name, email, phone, address, description, 
                createdFrom, createdTo, updatedFrom, updatedTo);

        Page<Suppliers> supplierPage = suppliersRepository.findAll(spec, pageable);

        List<SupplierResponseDTO> supplierDTOs = suppliersMapper.toResponseDTOList(supplierPage.getContent());

        // Create pagination info with proper current_page calculation
        int currentPage = supplierDTOs.isEmpty() ? 0 : page;
        int totalPages = supplierPage.getTotalPages();
        if (totalPages == 0) totalPages = 1;
        
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPage(currentPage);
        paginationInfo.setPerPage(perPage);
        paginationInfo.setTotal((int) supplierPage.getTotalElements());
        paginationInfo.setLastPage(totalPages);
        
        return PaginatedResponse.of(supplierDTOs, paginationInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<SupplierResponseDTO> findByBranchId(
            String branchId, int page, int perPage) {
        
        log.debug("Finding suppliers by branch ID: {}, page: {}, perPage: {}", branchId, page, perPage);

        // Validate and adjust pagination parameters
        page = Math.max(1, page);
        perPage = Math.min(Math.max(1, perPage), 100);

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(Sort.Direction.ASC, "name"));

        Page<Suppliers> supplierPage = suppliersRepository.findByBranchId(branchId, pageable);

        List<SupplierResponseDTO> supplierDTOs = suppliersMapper.toResponseDTOList(supplierPage.getContent());

        // Create pagination info with proper current_page calculation
        int currentPage = supplierDTOs.isEmpty() ? 0 : page;
        int totalPages = supplierPage.getTotalPages();
        if (totalPages == 0) totalPages = 1;
        
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPage(currentPage);
        paginationInfo.setPerPage(perPage);
        paginationInfo.setTotal((int) supplierPage.getTotalElements());
        paginationInfo.setLastPage(totalPages);
        
        return PaginatedResponse.of(supplierDTOs, paginationInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponseDTO findByIdOrThrow(String id) {
        log.debug("Finding supplier by ID: {}", id);
        
        Suppliers supplier = suppliersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SUPPLIER_ENTITY, id));
        
        return suppliersMapper.toResponseDTO(supplier);
    }

    @Override
    public SupplierResponseDTO create(SupplierCreateDTO createDTO) {
        log.info("Creating new supplier with name: {}", createDTO.getName());

        try {
            // Check if supplier with same name already exists
            if (suppliersRepository.existsByName(createDTO.getName())) {
                throw new IllegalArgumentException("Supplier with name '" + createDTO.getName() + "' already exists");
            }

            // Check if supplier with same email already exists (if email is provided)
            if (createDTO.getEmail() != null && !createDTO.getEmail().trim().isEmpty() 
                    && suppliersRepository.existsByEmail(createDTO.getEmail())) {
                throw new IllegalArgumentException("Supplier with email '" + createDTO.getEmail() + "' already exists");
            }

            // Validate additional info if provided
            if (createDTO.getAdditionalInfo() != null) {
                log.debug("Validating supplier additional info");
                validateAdditionalInfo(createDTO.getAdditionalInfo());
            }

            Suppliers supplier = suppliersMapper.toEntity(createDTO);
            supplier.setId(UUID.randomUUID().toString());
            supplier.setCreatedAt(LocalDateTime.now());
            supplier.setUpdatedAt(LocalDateTime.now());

            // Handle additional info conversion manually
            if (createDTO.getAdditionalInfo() != null) {
                String additionalInfoJson = suppliersMapper.additionalInfoToJson(createDTO.getAdditionalInfo());
                supplier.setAdditionalInfo(additionalInfoJson);
            }

            Suppliers savedSupplier = suppliersRepository.save(supplier);
            
            log.info("Successfully created supplier with ID: {}", savedSupplier.getId());
            return convertToResponseDTO(savedSupplier);
            
        } catch (Exception e) {
            log.error("Error creating supplier: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public SupplierResponseDTO update(String id, SupplierUpdateDTO updateDTO) {
        log.info("Updating supplier with ID: {}", id);

        try {
            Suppliers existingSupplier = suppliersRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(SUPPLIER_ENTITY, id));

            // Check if another supplier with same name already exists
            if (!existingSupplier.getName().equals(updateDTO.getName()) 
                    && suppliersRepository.existsByName(updateDTO.getName())) {
                throw new IllegalArgumentException("Supplier with name '" + updateDTO.getName() + "' already exists");
            }

            // Check if another supplier with same email already exists (if email is provided)
            if (updateDTO.getEmail() != null && !updateDTO.getEmail().trim().isEmpty()
                    && !updateDTO.getEmail().equals(existingSupplier.getEmail())
                    && suppliersRepository.existsByEmail(updateDTO.getEmail())) {
                throw new IllegalArgumentException("Supplier with email '" + updateDTO.getEmail() + "' already exists");
            }

            // Validate additional info if provided
            if (updateDTO.getAdditionalInfo() != null) {
                log.debug("Validating supplier additional info for update");
                validateAdditionalInfo(updateDTO.getAdditionalInfo());
            }

            suppliersMapper.updateEntityFromDTO(updateDTO, existingSupplier);
            existingSupplier.setUpdatedAt(LocalDateTime.now());

            // Handle additional info conversion manually
            if (updateDTO.getAdditionalInfo() != null) {
                String additionalInfoJson = suppliersMapper.additionalInfoToJson(updateDTO.getAdditionalInfo());
                existingSupplier.setAdditionalInfo(additionalInfoJson);
            }

            Suppliers updatedSupplier = suppliersRepository.save(existingSupplier);
            
            log.info("Successfully updated supplier with ID: {}", updatedSupplier.getId());
            return convertToResponseDTO(updatedSupplier);
            
        } catch (Exception e) {
            log.error("Error updating supplier: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void delete(String id) {
        log.info("Deleting supplier with ID: {}", id);

        Suppliers supplier = suppliersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SUPPLIER_ENTITY, id));

        // Check if supplier has any inventory movements
        if (supplier.getMovements() != null && !supplier.getMovements().isEmpty()) {
            throw new DeleteConstraintException(SUPPLIER_ENTITY, id, "has associated inventory movements");
        }

        suppliersRepository.delete(supplier);
        log.info("Successfully deleted supplier with ID: {}", id);
    }

    /**
     * Maps API sort field names to entity field names.
     */
    private String mapSortField(String sortField) {
        if (sortField == null || sortField.trim().isEmpty()) {
            return "createdAt";
        }
        
        return switch (sortField.toLowerCase()) {
            case "created_at" -> CREATED_AT_FIELD;
            case "updated_at" -> "updatedAt";
            case "name", "email", "phone", "address", "description" -> sortField;
            default -> CREATED_AT_FIELD;
        };
    }

    /**
     * Builds JPA Specification for dynamic filtering.
     */
    private Specification<Suppliers> buildSpecification(
            String branchId, String search, String name, String email, String phone, String address, String description,
            String createdFrom, String createdTo, String updatedFrom, String updatedTo) {
        
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Branch filter
            if (branchId != null && !branchId.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("branchId"), branchId.trim()));
            }

            // Global search across name, email, and phone
            if (search != null && !search.trim().isEmpty()) {
                String searchTerm = "%" + search.trim().toLowerCase() + "%";
                Predicate searchPredicate = criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchTerm),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchTerm),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), searchTerm)
                );
                predicates.add(searchPredicate);
            }

            // Individual field filters
            if (name != null && !name.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")), 
                        "%" + name.trim().toLowerCase() + "%"));
            }

            if (email != null && !email.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("email")), 
                        "%" + email.trim().toLowerCase() + "%"));
            }

            if (phone != null && !phone.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("phone")), 
                        "%" + phone.trim().toLowerCase() + "%"));
            }

            if (address != null && !address.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("address")), 
                        "%" + address.trim().toLowerCase() + "%"));
            }

            if (description != null && !description.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")), 
                        "%" + description.trim().toLowerCase() + "%"));
            }

            // Date range filters
            addDateRangeFilter(predicates, criteriaBuilder, root, CREATED_AT_FIELD, createdFrom, createdTo);
            addDateRangeFilter(predicates, criteriaBuilder, root, "updatedAt", updatedFrom, updatedTo);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        log.debug("Checking if supplier exists with ID: {}", id);
        return suppliersRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEmailTaken(String email, String excludeId) {
        log.debug("Checking if email is taken: {}, excluding ID: {}", email, excludeId);
        
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        if (excludeId != null && !excludeId.trim().isEmpty()) {
            return suppliersRepository.existsByEmailIgnoreCaseAndIdNot(email.trim(), excludeId.trim());
        } else {
            return suppliersRepository.existsByEmailIgnoreCase(email.trim());
        }
    }

    /**
     * Helper method to add date range filters to predicates.
     */
    private void addDateRangeFilter(List<Predicate> predicates, 
            jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
            jakarta.persistence.criteria.Root<Suppliers> root,
            String fieldName, String fromDate, String toDate) {
        
        try {
            if (fromDate != null && !fromDate.trim().isEmpty()) {
                LocalDateTime from = LocalDateTime.parse(fromDate.trim());
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), from));
            }
            
            if (toDate != null && !toDate.trim().isEmpty()) {
                LocalDateTime to = LocalDateTime.parse(toDate.trim());
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(fieldName), to));
            }
        } catch (DateTimeParseException e) {
            log.warn("Invalid date format for field {}: from={}, to={}", fieldName, fromDate, toDate);
            // Skip invalid date filters rather than throwing exception
        }
    }

    /**
     * Validates additional info using the professional validator.
     * 
     * @param additionalInfo the additional info to validate
     * @throws RuntimeException if validation fails
     */
    private void validateAdditionalInfo(SupplierAdditionalInfoDTO additionalInfo) {
        try {
            additionalInfoValidator.validate(additionalInfo);
        } catch (Exception e) {
            log.error("Additional info validation failed", e);
            throw new RuntimeException("Additional info validation failed: " + e.getMessage(), e);
        }
    }

    /**
     * Converts a Suppliers entity to SupplierResponseDTO with proper additionalInfo handling.
     * 
     * @param supplier the supplier entity
     * @return the response DTO
     */
    private SupplierResponseDTO convertToResponseDTO(Suppliers supplier) {
        SupplierResponseDTO responseDTO = suppliersMapper.toResponseDTO(supplier);
        
        // Handle additional info conversion manually
        if (supplier.getAdditionalInfo() != null && !supplier.getAdditionalInfo().trim().isEmpty()) {
            SupplierAdditionalInfoDTO additionalInfo = suppliersMapper.jsonToAdditionalInfo(supplier.getAdditionalInfo());
            responseDTO.setAdditionalInfo(additionalInfo);
        }
        
        return responseDTO;
    }
}
