package com.oms.inventory_service.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class InventoryRequestDTO {

    private UUID tenantId;
    private UUID productId;
    private Integer quantity;
}
