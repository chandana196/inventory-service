package com.oms.inventory_service.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReserveStockRequestDTO {

    private UUID orderId;

    private UUID productId;

    private Integer quantity;
}
