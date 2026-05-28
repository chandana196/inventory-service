package com.oms.inventory_service.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "inventories")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID tenantId;

    @Column(unique = true, nullable = false)
    private UUID productId;

    private Integer availableQuantity;

    private Integer reservedQuantity;

    private Integer reorderLevel;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
