package com.example.stock.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.stock.dto.inventorymouvement.InventoryMovementCreateDTO;
import com.example.stock.dto.inventorymouvement.InventoryMovementResponseDTO;
import com.example.stock.dto.inventorymouvement.InventoryMovementSummaryDTO;
import com.example.stock.dto.inventorymouvement.InventoryMovementUpdateDTO;
import com.example.stock.entity.InventoryMovement;

/**
 * MapStruct mapper interface for InventoryMovement entity and DTO conversions.
 * Handles mapping between InventoryMovement entity and various DTOs with proper null handling.
 * Follows professional standards and aligns with frontend contract requirements.
 * 
 * @author Generated
 * @since 1.0
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface InventoryMovementMapper {

    /**
     * Maps InventoryMovementCreateDTO to InventoryMovement entity.
     * Used when creating a new inventory movement from client request.
     * Note: InventoryItem and Supplier relationships need to be set separately in service layer.
     *
     * @param createDTO the inventory movement creation data
     * @return InventoryMovement entity ready for persistence
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "inventoryItem", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "destinationDepartment", ignore = true)
    @Mapping(target = "destinationBranch", ignore = true)
    @Mapping(target = "departmentId", ignore = true)
    @Mapping(target = "destinationDepartmentId", ignore = true)
    InventoryMovement toEntity(InventoryMovementCreateDTO createDTO);

    /**
     * Maps InventoryMovement entity to InventoryMovementResponseDTO.
     * Used when returning inventory movement data to client with embedded relationships.
     * Note: Complex nested objects are handled by the DTO's fromEntity method.
     * 
     * @param inventoryMovement the inventory movement entity
     * @return InventoryMovementResponseDTO with complete inventory movement information
     */
    default InventoryMovementResponseDTO toResponseDTO(InventoryMovement inventoryMovement) {
        if (inventoryMovement == null) {
            return null;
        }
        return InventoryMovementResponseDTO.fromEntity(inventoryMovement);
    }

    /**
     * Maps InventoryMovement entity to InventoryMovementSummaryDTO.
     * Used for lightweight inventory movement listings.
     * 
     * @param inventoryMovement the inventory movement entity
     * @return InventoryMovementSummaryDTO with essential inventory movement information
     */
    InventoryMovementSummaryDTO toSummaryDTO(InventoryMovement inventoryMovement);

    /**
     * Maps InventoryMovementUpdateDTO to existing InventoryMovement entity.
     * Updates only the provided fields, ignoring null values.
     * Note: Core relationships (InventoryStock, Supplier) need to be handled separately if changed.
     *
     * @param updateDTO the inventory movement update data
     * @param inventoryMovement the existing inventory movement entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "transactionType", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "unitPurchasePrice", ignore = true) // Not updatable in UpdateDTO
    @Mapping(target = "inventoryItem", ignore = true)
    @Mapping(target = "inventoryItemId", ignore = true)
    @Mapping(target = "destinationBranchId", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "destinationDepartment", ignore = true)
    @Mapping(target = "destinationBranch", ignore = true)
    @Mapping(target = "departmentId", ignore = true)
    @Mapping(target = "destinationDepartmentId", ignore = true)
    void updateEntityFromDTO(InventoryMovementUpdateDTO updateDTO, @MappingTarget InventoryMovement inventoryMovement);

    /**
     * Maps list of InventoryMovement entities to list of InventoryMovementResponseDTOs.
     * 
     * @param inventoryMovements list of inventory movement entities
     * @return list of InventoryMovementResponseDTOs
     */
    List<InventoryMovementResponseDTO> toResponseDTOList(List<InventoryMovement> inventoryMovements);

    /**
     * Maps list of InventoryMovement entities to list of InventoryMovementSummaryDTOs.
     * 
     * @param inventoryMovements list of inventory movement entities
     * @return list of InventoryMovementSummaryDTOs
     */
    List<InventoryMovementSummaryDTO> toSummaryDTOList(List<InventoryMovement> inventoryMovements);
}
