package com.assessment.shop_warehouse_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDto {
    private long totalItems;
    private long totalVariants;
    private long lowStock;
    private long todaySales;
}
