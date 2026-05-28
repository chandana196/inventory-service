package com.oms.inventory_service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stock_reservations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockReservation {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID orderId;

    private UUID productId;

    private Integer quantity;

    private String status;

    private LocalDateTime createdAt;
}
