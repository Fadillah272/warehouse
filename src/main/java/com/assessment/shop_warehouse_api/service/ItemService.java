package com.assessment.shop_warehouse_api.service;

import com.assessment.shop_warehouse_api.common.response.ApiResponse;
import com.assessment.shop_warehouse_api.dto.ItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemService {
    ApiResponse<ItemDto> createItem(ItemDto itemDto);
    ApiResponse<ItemDto> updateItem(Long id, ItemDto itemDto);
    ApiResponse<String> deleteItem(Long id);
    ApiResponse<Page<ItemDto>> getAllItem(String keyword, Pageable pageable);
    ApiResponse<java.util.List<ItemDto>> getAllDeleteItem();
    ApiResponse<ItemDto> getItemById(Long id);
}
