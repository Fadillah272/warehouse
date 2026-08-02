package com.assessment.shop_warehouse_api.service;

import com.assessment.shop_warehouse_api.common.response.ApiResponse;
import com.assessment.shop_warehouse_api.dto.ItemDto;
import com.assessment.shop_warehouse_api.dto.ItemVariantDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VariantService {
    ApiResponse<ItemVariantDto> createVariant(Long itemId, ItemVariantDto dto);
    ApiResponse<ItemVariantDto> updateVariant(Long id, ItemVariantDto dto);
    ApiResponse<String> deleteVariant(Long id);
    ApiResponse<List<ItemVariantDto>> getVariantsByItemId(Long itemId);
    ApiResponse<List<ItemVariantDto>> getAllDeleteVariant();
    ApiResponse<Page<ItemVariantDto>> getAllItem(String keyword, Pageable pageable);
    ApiResponse<ItemVariantDto> getItemByBarcode(String barcode);
}
