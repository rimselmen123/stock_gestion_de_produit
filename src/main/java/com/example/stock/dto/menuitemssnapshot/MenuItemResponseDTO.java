package com.example.stock.dto.menuitemssnapshot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemResponseDTO {
    private Long id;
    private Long posMenuItemId;
    private String name;
    private Long branchId;
}
