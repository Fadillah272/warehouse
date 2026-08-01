package com.assessment.shop_warehouse_api.repository;

import com.assessment.shop_warehouse_api.entity.ItemVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemVariantRepository extends JpaRepository<ItemVariant, Long> {
    boolean existsByBarcode(String barcode);
    List<ItemVariant> findByItemIdAndIsDeletedFalse(Long itemId);
    List<ItemVariant> findAllByIsDeletedFalse();
    List<ItemVariant> findAllByIsDeletedTrue();
    List<ItemVariant> findByStockLessThanEqualAndIsDeletedFalse(Integer threshold);
}
