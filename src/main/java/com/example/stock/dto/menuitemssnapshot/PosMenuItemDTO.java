package com.example.stock.dto.menuitemssnapshot;

import java.util.List;
import com.example.stock.dto.variation.VariationDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Data
@Setter
@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PosMenuItemDTO {

    private Long id;
    private String item_name;
    private String image;
    private Long branch_id;
    private String updated_at;
    private String item_photo_url;
    private List<VariationDTO> variations;

    
}