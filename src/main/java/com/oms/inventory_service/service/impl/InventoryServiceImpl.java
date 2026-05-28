package com.oms.inventory_service.service.impl;

import com.oms.inventory_service.dto.request.InventoryRequestDTO;
import com.oms.inventory_service.dto.request.ReserveStockRequestDTO;
import com.oms.inventory_service.dto.request.UpdateReorderLevelRequestDTO;
import com.oms.inventory_service.dto.request.UpdateStockRequestDTO;
import com.oms.inventory_service.dto.response.InventoryAvailbilityResponseDTO;
import com.oms.inventory_service.dto.response.InventoryResponseDTO;
import com.oms.inventory_service.entities.Inventory;
import com.oms.inventory_service.entities.InventoryTransaction;
import com.oms.inventory_service.entities.StockReservation;
import com.oms.inventory_service.repositories.InventoryRepository;
import com.oms.inventory_service.repositories.StockReservationRepository;
import com.oms.inventory_service.repositories.TransactionRepository;
import com.oms.inventory_service.service.InventoryService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    private final TransactionRepository transactionRepository;

    private final StockReservationRepository stockReservationRepository;


    @Override
    public InventoryResponseDTO createInventory(InventoryRequestDTO request){

        Inventory inventory = Inventory.builder()
                .tenantId(request.getTenantId())
                .productId(request.getProductId())
                .availableQuantity(request.getQuantity())
                .reservedQuantity(0)
                .reorderLevel(5)
                .createdAt(LocalDateTime.now())
                .build();

        inventoryRepository.save(inventory);

        createTransaction(
                inventory,
                "STOCK_CREATED",
                request.getQuantity(),
                null,
                "Initial stock created"
        );

        return mapToResponse(inventory);
    }

    @Override
    public List<InventoryResponseDTO> getAllInventories() {
        return inventoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public InventoryResponseDTO getInventory(UUID productId){

            Inventory inventory = inventoryRepository
                    .findByProductId(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

        return mapToResponse(inventory);
    }

    @Override
    public InventoryAvailbilityResponseDTO checkAvailability(UUID productId){

            Inventory inventory = inventoryRepository
                    .findByProductId(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

         return InventoryAvailbilityResponseDTO.builder()
                 .available(inventory.getAvailableQuantity() > 0)
                 .quantity(inventory.getAvailableQuantity())
                 .build();
    }

    @Override
    public InventoryResponseDTO addStock(UUID productId,
                                         UpdateStockRequestDTO request){

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        inventory.setAvailableQuantity(inventory.getAvailableQuantity() +
                                        request.getQuantity());
        inventory.setUpdatedAt(LocalDateTime.now());

        createTransaction(
                inventory,
                "STOCK_ADDED",
                request.getQuantity(),
                null,
                "Stock added"
        );

        return mapToResponse(inventory);
    }

    @Override
    public InventoryResponseDTO reduceStock(UUID productId,
                                            UpdateStockRequestDTO request){
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if(inventory.getAvailableQuantity() < request.getQuantity()){
            throw new RuntimeException("Insufficient Stock");
        }

        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - request.getQuantity());

        inventory.setUpdatedAt(LocalDateTime.now());

        createTransaction(
                inventory,
                "Stock_reduced",
                request.getQuantity(),
                null,
                "Manual stock reduction"
        );

        return mapToResponse(inventory);


    }

    @Override
    public InventoryResponseDTO updateReorderLevel(UUID productId,
                                                   UpdateReorderLevelRequestDTO request){

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(()-> new RuntimeException("Product not found"));

        inventory.setReorderLevel(request.getReorderLevel());
        inventoryRepository.save(inventory);

        return mapToResponse(inventory);
    }

    @Override
    public List<InventoryResponseDTO> getLowStockProducts(){

        return inventoryRepository.findAll()
                .stream()
                .filter(e ->
                        e.getAvailableQuantity() <= e.getReorderLevel())
                .map(this::mapToResponse)
                .toList();
    }

    public void reserveStock(ReserveStockRequestDTO request){

        Inventory inventory = inventoryRepository.findByProductId(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if(inventory.getAvailableQuantity() < request.getQuantity()){
            throw new RuntimeException("Insufficient stock");
        }

        inventory.setAvailableQuantity(
                inventory.getAvailableQuantity()-request.getQuantity());

        inventory.setReservedQuantity(
                inventory.getReservedQuantity()+request.getQuantity());

        inventoryRepository.save(inventory);

        StockReservation reservation =
                StockReservation.builder()
                        .orderId(request.getOrderId())
                        .productId(request.getProductId())
                        .quantity(request.getQuantity())
                        .status("RESERVED")
                        .createdAt(LocalDateTime.now())
                        .build();

        stockReservationRepository.save(reservation);

        createTransaction(
                inventory,
                "STOCK_RESERVED",
                request.getQuantity(),
                request.getOrderId(),
                "Stock reserved"
        );
    }

    @Override
    public void releaseReservedStock(UUID orderId) {

        List<StockReservation> reservations =
                stockReservationRepository.findByOrderId(orderId);

        for(StockReservation reservation : reservations){

            Inventory inventory = inventoryRepository.findByProductId(reservation.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            inventory.setAvailableQuantity(
                    inventory.getAvailableQuantity()+reservation.getQuantity()
            );

            inventory.setReservedQuantity(
                    inventory.getReservedQuantity()- reservation.getQuantity()
            );

            inventoryRepository.save(inventory);

            reservation.setStatus("RELEASED");

            createTransaction(
                    inventory,
                    "STOCK_RELEASED",
                    reservation.getQuantity(),
                    orderId,
                    "Reserved stock released"
            );
        }
    }

    @Override
    public void commitReservedStock(UUID orderId) {

        List<StockReservation> reservations =
                stockReservationRepository.findByOrderId(orderId);

        for (StockReservation reservation : reservations){

            Inventory inventory = inventoryRepository.findByProductId(reservation.getProductId())
                    .orElseThrow(()-> new RuntimeException("Product not found"));

            inventory.setReservedQuantity(
                    inventory.getReservedQuantity()-
                            reservation.getQuantity());

            inventoryRepository.save(inventory);

            reservation.setStatus("COMMITED");

            createTransaction(
                    inventory,
                    "STOCK_COMMITED",
                    reservation.getQuantity(),
                    orderId,
                    "Stock permanently deducted"
            );
        }
    }

    private InventoryResponseDTO mapToResponse(Inventory inventory){

        return InventoryResponseDTO.builder()
                .productId(inventory.getProductId())
                .availableQuantity(inventory.getAvailableQuantity())
                .reservedQuantity(inventory.getReservedQuantity())
                .reorderLevel(inventory.getReorderLevel())
                .build();
    }

    private void createTransaction(
            Inventory inventory,
            String type,
            Integer quantity,
            UUID referenceId,
            String remarks){

        InventoryTransaction transaction =
                InventoryTransaction.builder()
                        .inventoryId(inventory.getId())
                        .productId(inventory.getProductId())
                        .transcationType(type)
                        .quantity(quantity)
                        .referenceId(referenceId)
                        .remarks(remarks)
                        .createdAt(LocalDateTime.now())
                        .build();

        transactionRepository.save(transaction);

    }
}
