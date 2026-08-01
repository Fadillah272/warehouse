package com.assessment.shop_warehouse_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDto {
    private Long variantId;
    private String variantName;
    private String barcode;
    private Integer stock;
}
