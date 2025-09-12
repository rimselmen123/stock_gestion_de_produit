package com.example.stock.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.stock.dto.department.DepartmentCreateDTO;
import com.example.stock.dto.department.DepartmentResponseDTO;
import com.example.stock.dto.department.DepartmentSummaryDTO;
import com.example.stock.dto.department.DepartmentUpdateDTO;
import com.example.stock.entity.Department;
import com.example.stock.exception.DuplicateResourceException;
import com.example.stock.exception.ResourceNotFoundException;
import com.example.stock.mapper.DepartmentMaapper;
import com.example.stock.repository.BranchRepository;
import com.example.stock.repository.DepartmentRepository;
import com.example.stock.repository.InventoryItemCategoryRepository;
import com.example.stock.repository.InventoryMovementRepository;
import com.example.stock.repository.InventoryStockRepository;
import com.example.stock.service.DepartmentService;
import com.example.stock.specification.DepartementSpecifications;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    private static final String DEPT_NOT_FOUND = "Department not found with id: ";

    private final DepartmentRepository departmentRepository;
    private final BranchRepository branchRepository;
    private final InventoryItemCategoryRepository categoryRepository;
    private final InventoryMovementRepository movementRepository;
    private final InventoryStockRepository stockRepository;
    private final DepartmentMaapper departmentMapper;

    @Override
    public DepartmentResponseDTO create(DepartmentCreateDTO dto) {
        log.info("Creating department with name: {} for branch: {}", dto.getName(), dto.getBranchId());
        
        // Validate branch exists
        branchRepository.findById(dto.getBranchId())
            .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + dto.getBranchId()));
        
        // Check uniqueness (name, branchId)
        if (departmentRepository.existsByBranchIdAndNameIgnoreCase(dto.getBranchId(), dto.getName())) {
            throw new DuplicateResourceException("Department with name '" + dto.getName() + "' already exists in this branch");
        }
        
        // Convert DTO to entity
        Department department = departmentMapper.toEntity(dto);
        department.setId(UUID.randomUUID().toString());
        department.setCreatedAt(LocalDateTime.now());
        department.setUpdatedAt(LocalDateTime.now());
        
        // Save and return
        Department saved = departmentRepository.save(department);
        return departmentMapper.toResponseDTO(saved);
    }

    @Override
    public DepartmentResponseDTO update(String id, DepartmentUpdateDTO dto) {
        log.info("Updating department with id: {}", id);
        
        Department existing = departmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(DEPT_NOT_FOUND + id));
        
        // Check name uniqueness if name is being changed
        if (dto.getName() != null && !dto.getName().equals(existing.getName()) 
            && departmentRepository.existsByBranchIdAndNameIgnoreCase(existing.getBranchId(), dto.getName())) {
            throw new DuplicateResourceException("Department with name '" + dto.getName() + "' already exists in this branch");
        }
        
        // Update entity (branchId immutable)
        departmentMapper.updateEntityFromDTO(dto, existing);
        existing.setUpdatedAt(LocalDateTime.now());
        
        Department saved = departmentRepository.save(existing);
        return departmentMapper.toResponseDTO(saved);
    }

    @Override
    public void delete(String id) {
        log.info("Deleting department with id: {}", id);
        
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(DEPT_NOT_FOUND + id));
        
        // Check if department can be deleted
        if (!canDeleteInternal(id)) {
            throw new IllegalStateException("Cannot delete department. It has categories, movements, or stocks");
        }
        
        departmentRepository.delete(department);
        log.info("Department deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponseDTO findById(String id) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(DEPT_NOT_FOUND + id));
        return departmentMapper.toResponseDTO(department);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentResponseDTO> search(DepartmentFilterDTO filter) {
        // Build specifications
        Specification<Department> spec = DepartementSpecifications.build(
            filter.getSearch(), 
            filter.getName(), 
            filter.getBranchId(), 
            filter.getCreatedFrom(), 
            filter.getCreatedTo()
        );
        
        // Build sort
        Sort sort = Sort.by(
            "DESC".equalsIgnoreCase(filter.getSortDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC,
            filter.getSortField()
        );
        
        PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getSize(), sort);
        
        Page<Department> departments = departmentRepository.findAll(spec, pageRequest);
        return departments.map(departmentMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentSummaryDTO> listByBranch(String branchId) {
        List<Department> departments = departmentRepository.findByBranchId(branchId, PageRequest.of(0, 1000)).getContent();
        return departmentMapper.toSummaryDTOList(departments);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canDelete(String id) {
        return canDeleteInternal(id);
    }

    private boolean canDeleteInternal(String id) {
        return !categoryRepository.existsByDepartmentId(id) 
            && !movementRepository.existsByDepartmentId(id) 
            && !movementRepository.existsByDestinationDepartmentId(id)
            && !stockRepository.existsByDepartmentId(id);
    }
}
