package com.assessment.shop_warehouse_api.service;

import com.assessment.shop_warehouse_api.common.response.ApiResponse;
import com.assessment.shop_warehouse_api.dto.DashboardDto;

public interface DashboardService {
    ApiResponse<DashboardDto> getSummary();
}
