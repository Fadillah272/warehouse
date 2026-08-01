package com.assessment.shop_warehouse_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockAdjustmentDto {

    @NotNull(message = "Variant id wajib diisi")
    private Long variantId;

    @NotNull(message = "Jumlah penyesuaian wajib diisi")
    private Integer adjustment;

    private String referenceNumber;
}
