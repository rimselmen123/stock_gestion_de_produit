package com.example.stock.dto.suppliers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a new Supplier.
 * Contains only the fields required for supplier creation.
 * 
 * @author Generated
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierCreateDTO {

    @NotBlank(message = "Name is required")
    private String name;
   //lbe9i hethom lkoll optionnels 
    @Email(message = "invalide format email")
    private String email;

    private String phone;

    private String address;

    private String description;
}
