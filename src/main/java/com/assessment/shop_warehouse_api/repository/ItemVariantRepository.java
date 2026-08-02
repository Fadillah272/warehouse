package com.assessment.shop_warehouse_api.repository;

import com.assessment.shop_warehouse_api.entity.Item;
import com.assessment.shop_warehouse_api.entity.ItemVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemVariantRepository extends JpaRepository<ItemVariant, Long> {
    boolean existsByBarcode(String barcode);
    List<ItemVariant> findByItemIdAndIsDeletedFalse(Long itemId);
    List<ItemVariant> findAllByIsDeletedFalse();
    List<ItemVariant> findAllByIsDeletedTrue();
    List<ItemVariant> findByStockLessThanEqualAndIsDeletedFalse(Integer threshold);
    @Query(value = "SELECT * FROM T_ITEM_VARIANT WHERE BARCODE = :barcode", nativeQuery = true)
    List<ItemVariant> findAllByBarcode(@Param("barcode") String barcode);
    Page<ItemVariant> findByIsDeletedFalseAndVariantNameContainingIgnoreCase(String keyword, Pageable pageable);
    Page<ItemVariant> findByIsDeletedFalse(Pageable pageable);
}
