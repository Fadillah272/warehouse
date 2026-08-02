package com.assessment.shop_warehouse_api.repository;

import com.assessment.shop_warehouse_api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByIsDeletedFalse();
    List<Category> findAllByIsDeletedTrue();

}
