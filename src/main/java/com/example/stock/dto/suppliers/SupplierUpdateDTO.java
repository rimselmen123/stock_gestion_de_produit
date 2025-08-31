package com.example.stock.dto.suppliers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for updating an existing Unit.
 * Contains only the fields that can be updated.
 * 
 * @author Generated
 * @since 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierUpdateDTO {
    @NotBlank(message = "Name is required")
    private String name;
   //kime fel post 
    @Email(message = "invalide format email")
    private String email;

    private String phone;

    private String address;

    private String description;
}
