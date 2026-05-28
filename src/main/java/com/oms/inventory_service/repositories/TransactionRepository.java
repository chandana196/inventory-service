package com.oms.inventory_service.repositories;

import com.oms.inventory_service.entities.Inventory;
import com.oms.inventory_service.entities.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<InventoryTransaction, UUID> {

    List<InventoryTransaction> findByProductId(UUID productId);
}
