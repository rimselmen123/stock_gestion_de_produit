package com.example.stock.mapper;

import com.example.stock.dto.suppliers.SupplierAdditionalInfoDTO;
import com.example.stock.dto.suppliers.SupplierCreateDTO;
import com.example.stock.dto.suppliers.SupplierResponseDTO;
import com.example.stock.dto.suppliers.SupplierSummaryDTO;
import com.example.stock.dto.suppliers.SupplierUpdateDTO;
import com.example.stock.entity.Suppliers;
import com.example.stock.util.JsonUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * MapStruct mapper interface for Suppliers entity and DTO conversions.
 * Handles mapping between Suppliers entity and various DTOs with proper null handling.
 * Provides professional JSON conversion for additionalInfo field.
 * 
 * @author Development Team Blink
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
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "additionalInfo", ignore = true)
    Suppliers toEntity(SupplierCreateDTO createDTO);

    /**
     * Maps Suppliers entity to SupplierResponseDTO.
     * Used when returning supplier data to client.
     * 
     * @param supplier the supplier entity
     * @return SupplierResponseDTO with complete supplier information
     */
    @Mapping(target = "additionalInfo", source = "additionalInfo", qualifiedByName = "jsonToAdditionalInfo")
    SupplierResponseDTO toResponseDTO(Suppliers supplier);
    
    /**
     * Qualifier method for JSON to AdditionalInfo conversion in MapStruct.
     */
    @org.mapstruct.Named("jsonToAdditionalInfo")
    default SupplierAdditionalInfoDTO mapJsonToAdditionalInfo(String json) {
        return jsonToAdditionalInfo(json);
    }

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
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "additionalInfo", ignore = true)
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

    /**
     * Converts SupplierAdditionalInfoDTO to JSON string for database storage.
     * Handles null values gracefully and provides error handling.
     * 
     * @param additionalInfo the additional info DTO
     * @return JSON string representation or null if input is null
     */
    default String additionalInfoToJson(SupplierAdditionalInfoDTO additionalInfo) {
        if (additionalInfo == null) {
            return null;
        }
        return JsonUtils.toJson(additionalInfo);
    }

    /**
     * Converts JSON string from database to SupplierAdditionalInfoDTO.
     * Handles null values and invalid JSON gracefully.
     * 
     * @param json the JSON string from database
     * @return SupplierAdditionalInfoDTO or null if input is null/invalid
     */
    default SupplierAdditionalInfoDTO jsonToAdditionalInfo(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        return JsonUtils.fromJson(json, SupplierAdditionalInfoDTO.class);
    }
}
