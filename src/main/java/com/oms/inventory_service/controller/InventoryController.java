package com.oms.inventory_service.controller;

import com.oms.inventory_service.dto.request.UpdateStockRequestDTO;
import com.oms.inventory_service.dto.response.InventoryAvailbilityResponseDTO;
import com.oms.inventory_service.dto.response.InventoryResponseDTO;
import com.oms.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{productId}")
    public InventoryResponseDTO getInventory(
            @PathVariable UUID productId){

        return inventoryService.getInventory(productId);
    }

    @GetMapping("/availability/{productId}")
    public InventoryAvailbilityResponseDTO checkAvailiability(
            @PathVariable UUID productId){

        return inventoryService.checkAvailability(productId);
    }



}
