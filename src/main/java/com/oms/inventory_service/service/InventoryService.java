package com.oms.inventory_service.service;

import com.oms.inventory_service.dto.request.InventoryRequestDTO;
import com.oms.inventory_service.dto.request.ReserveStockRequestDTO;
import com.oms.inventory_service.dto.request.UpdateReorderLevelRequestDTO;
import com.oms.inventory_service.dto.request.UpdateStockRequestDTO;
import com.oms.inventory_service.dto.response.InventoryAvailbilityResponseDTO;
import com.oms.inventory_service.dto.response.InventoryResponseDTO;


import java.util.List;
import java.util.UUID;

public interface InventoryService {

    InventoryResponseDTO createInventory(InventoryRequestDTO request);

    List<InventoryResponseDTO> getAllInventories();

    InventoryResponseDTO getInventory(UUID productId);

    InventoryAvailbilityResponseDTO checkAvailability(UUID productId);

    InventoryResponseDTO addStock(UUID productId, UpdateStockRequestDTO request);

    InventoryResponseDTO reduceStock(UUID productId,UpdateStockRequestDTO request);

    InventoryResponseDTO updateReorderLevel(UUID productId, UpdateReorderLevelRequestDTO request);

    List<InventoryResponseDTO> getLowStockProducts();

    void reserveStock(ReserveStockRequestDTO request);

    void releaseReservedStock(UUID orderId);

    void commitReservedStock(UUID orderId);
}
