package com.oms.inventory_service.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryAvailbilityResponseDTO {

    private Boolean available;

    private Integer quantity;
}
