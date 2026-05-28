package com.oms.inventory_service.controller;

import com.oms.inventory_service.dto.request.ReserveStockRequestDTO;
import com.oms.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class StockReservationController {

    private final InventoryService inventoryService;

    @PostMapping("/reserve")
    public ResponseEntity<String> reserveStock(
            @RequestBody ReserveStockRequestDTO request){

        inventoryService.reserveStock(request);

        return ResponseEntity.ok("Stock Reserved");
    }

    @PostMapping("/release/{orderId}")
    public ResponseEntity<String> releaseReseredStock(
            @PathVariable UUID orderId){

        inventoryService.releaseReservedStock(orderId);

        return ResponseEntity.ok("Stock Released");
    }

    @PostMapping("/commit/{orderId}")
    public ResponseEntity<String> commitStock(
            @PathVariable UUID orderId){

        inventoryService.commitReservedStock(orderId);

        return ResponseEntity.ok("Stock Commited");
    }
}
