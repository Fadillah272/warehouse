package com.assessment.shop_warehouse_api.service;

import com.assessment.shop_warehouse_api.common.response.ApiResponse;
import com.assessment.shop_warehouse_api.dto.SaleDto;

import java.util.List;

public interface SalesService {
    ApiResponse<SaleDto> createSale(SaleDto saleDto);
    ApiResponse<List<SaleDto>> getAllSale();
    ApiResponse<SaleDto> getSaleById(Long id);
}
