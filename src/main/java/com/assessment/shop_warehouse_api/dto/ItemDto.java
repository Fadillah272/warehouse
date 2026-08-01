package com.assessment.shop_warehouse_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemDto {
    private Long id;

    @NotBlank(message = "SKU wajib diisi")
    private String sku;

    @NotBlank(message = "Nama item wajib diisi")
    private String nameItem;

    private String description;

    @NotNull(message = "Category id wajib diisi")
    private Long categoryId;

    private String categoryName;
    private boolean isDeleted;
}
