package com.example.stock.mapper;

import com.example.stock.dto.suppliers.SupplierCreateDTO;
import com.example.stock.dto.suppliers.SupplierResponseDTO;
import com.example.stock.dto.suppliers.SupplierSummaryDTO;
import com.example.stock.dto.suppliers.SupplierUpdateDTO;
import com.example.stock.entity.Suppliers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * MapStruct mapper interface for Suppliers entity and DTO conversions.
 * Handles mapping between Suppliers entity and various DTOs with proper null handling.
 * 
 * @author Generated
 * @since 1.0
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface SuppliersMapper {

    /**
     * Maps SupplierCreateDTO to Suppliers entity.
     * Used when creating a new supplier from client request.
     * 
     * @param createDTO the supplier creation data
     * @return Suppliers entity ready for persistence
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "movements", ignore = true)
    Suppliers toEntity(SupplierCreateDTO createDTO);

    /**
     * Maps Suppliers entity to SupplierResponseDTO.
     * Used when returning supplier data to client.
     * 
     * @param supplier the supplier entity
     * @return SupplierResponseDTO with complete supplier information
     */
    SupplierResponseDTO toResponseDTO(Suppliers supplier);

    /**
     * Maps Suppliers entity to SupplierSummaryDTO.
     * Used for lightweight supplier listings.
     * 
     * @param supplier the supplier entity
     * @return SupplierSummaryDTO with essential supplier information
     */
    SupplierSummaryDTO toSummaryDTO(Suppliers supplier);

    /**
     * Maps SupplierUpdateDTO to existing Suppliers entity.
     * Updates only the provided fields, ignoring null values.
     * 
     * @param updateDTO the supplier update data
     * @param supplier the existing supplier entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "movements", ignore = true)
    void updateEntityFromDTO(SupplierUpdateDTO updateDTO, @MappingTarget Suppliers supplier);

    /**
     * Maps list of Suppliers entities to list of SupplierResponseDTOs.
     * 
     * @param suppliers list of supplier entities
     * @return list of SupplierResponseDTOs
     */
    List<SupplierResponseDTO> toResponseDTOList(List<Suppliers> suppliers);

    /**
     * Maps list of Suppliers entities to list of SupplierSummaryDTOs.
     * 
     * @param suppliers list of supplier entities
     * @return list of SupplierSummaryDTOs
     */
    List<SupplierSummaryDTO> toSummaryDTOList(List<Suppliers> suppliers);
}
