package com.oms.inventory_service.controller;

import com.oms.inventory_service.dto.request.InventoryRequestDTO;
import com.oms.inventory_service.dto.request.UpdateReorderLevelRequestDTO;
import com.oms.inventory_service.dto.request.UpdateStockRequestDTO;
import com.oms.inventory_service.dto.response.InventoryResponseDTO;
import com.oms.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/inventory")
@RequiredArgsConstructor
public class AdminInventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public InventoryResponseDTO createInventory(
            @RequestBody InventoryRequestDTO request){

        return inventoryService.createInventory(request);
    }

    @GetMapping
    public List<InventoryResponseDTO> getAllInventories(){
        return inventoryService.getAllInventories();
    }

    @PutMapping("/{productId}/add-stock")
    public InventoryResponseDTO addStock(
            @PathVariable UUID productId,
            @RequestBody UpdateStockRequestDTO request){

        return inventoryService.addStock(productId,request);
    }

    @PutMapping("/{productId}/reduce-stock")
    public InventoryResponseDTO reduceStock(
            @PathVariable UUID productId,
            @RequestBody UpdateStockRequestDTO request){

        return inventoryService.reduceStock(productId,request);
    }

    @PutMapping("/{productId}/reorder-level")
    public InventoryResponseDTO updateReorderLevel(
            @PathVariable UUID productId,
            @RequestBody UpdateReorderLevelRequestDTO request){

        return inventoryService.updateReorderLevel(productId,request);
    }

    @GetMapping("/low-stock")
    public List<InventoryResponseDTO> getLowStockProducts(){

        return inventoryService.getLowStockProducts();
    }

}
