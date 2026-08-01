package com.assessment.shop_warehouse_api.service;

import com.assessment.shop_warehouse_api.common.response.ApiResponse;
import com.assessment.shop_warehouse_api.dto.StockAdjustmentDto;
import com.assessment.shop_warehouse_api.dto.StockDto;

import java.util.List;

public interface StockService {
    ApiResponse<List<StockDto>> getAllStock();
    ApiResponse<List<StockDto>> getLowStock(int threshold);
    ApiResponse<StockDto> adjustStock(StockAdjustmentDto dto);
}
