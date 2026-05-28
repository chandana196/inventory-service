package com.oms.inventory_service.controller;

import com.oms.inventory_service.entities.InventoryTransaction;
import com.oms.inventory_service.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/inventory/transactions")
@RequiredArgsConstructor
public class InventoryTransactionController {

    private final TransactionRepository repository;

    @GetMapping
    public List<InventoryTransaction> getAllTranactions(){
        return  repository.findAll();
    }


    @GetMapping("/{productId}")
    public List<InventoryTransaction> getProductTransaction(
            @PathVariable UUID productId){
        return repository.findByProductId(productId);
    }

}
