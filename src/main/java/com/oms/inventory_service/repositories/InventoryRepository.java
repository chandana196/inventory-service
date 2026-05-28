package com.oms.inventory_service.repositories;

import com.oms.inventory_service.entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<Inventory, UUID> {


    Optional<Inventory> findByProductId(UUID productId);
}
