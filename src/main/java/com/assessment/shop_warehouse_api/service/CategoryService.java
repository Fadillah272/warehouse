package com.assessment.shop_warehouse_api.service;

import com.assessment.shop_warehouse_api.common.response.ApiResponse;
import com.assessment.shop_warehouse_api.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    // Menambahkan kategori
    ApiResponse<CategoryDto> createCategory(CategoryDto categoryDto);
    // Mengupdate data
    ApiResponse<CategoryDto> updateCategory(Long id, CategoryDto categoryDto);
    // Soft delete
    ApiResponse<String> deleteCategory(Long id);
    // Mengambil semua kategori aktif
    ApiResponse<List<CategoryDto>> getAllCategory();
    // Mengambil semua kategori yang sudah dihapus (soft delete)
    ApiResponse<List<CategoryDto>> getAllDeleteCategory();
    // Mengambil kategori berdasarkan ID
    ApiResponse<CategoryDto> getCategoryById(Long id);
}
