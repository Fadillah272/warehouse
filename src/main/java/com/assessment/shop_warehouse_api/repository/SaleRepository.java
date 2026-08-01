package com.assessment.shop_warehouse_api.repository;

import com.assessment.shop_warehouse_api.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
