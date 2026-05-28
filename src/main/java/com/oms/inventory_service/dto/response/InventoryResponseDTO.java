package com.oms.inventory_service.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponseDTO {

    private UUID productId;
    private Integer availableQuantity;
    private Integer reservedQuantity;
    private Integer reorderLevel;
}
