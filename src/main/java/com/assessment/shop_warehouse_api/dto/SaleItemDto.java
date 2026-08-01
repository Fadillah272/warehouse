package com.assessment.shop_warehouse_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SaleItemDto {

    @NotNull(message = "Variant id wajib diisi")
    private Long variantId;

    @Min(value = 1, message = "Quantity minimal 1")
    private Integer quantity;
}
