package com.oms.inventory_service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Inventory_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryTransaction {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID inventoryId;

    private UUID productId;

    private String transcationType;

    private Integer quantity;

    private UUID referenceId;

    private String remarks;

    private LocalDateTime createdAt;
}
