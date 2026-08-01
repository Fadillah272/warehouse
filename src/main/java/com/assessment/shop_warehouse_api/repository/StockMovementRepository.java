package com.assessment.shop_warehouse_api.repository;

import com.assessment.shop_warehouse_api.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    List<StockMovement> findByVariantIdOrderByCreatedAtDesc(Long variantId);
}
