package com.oms.inventory_service.repositories;

import com.oms.inventory_service.entities.StockReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StockReservationRepository extends JpaRepository<StockReservation, UUID> {

    List<StockReservation> findByOrderId(UUID orderId);
}
