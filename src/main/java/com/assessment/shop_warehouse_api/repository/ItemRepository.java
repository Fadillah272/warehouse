package com.assessment.shop_warehouse_api.repository;

import com.assessment.shop_warehouse_api.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    boolean existsBySku(String sku);
    List<Item> findAllByIsDeletedTrue();
    Page<Item> findByIsDeletedFalseAndNameItemContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Item> findByIsDeletedFalse(Pageable pageable);
}
