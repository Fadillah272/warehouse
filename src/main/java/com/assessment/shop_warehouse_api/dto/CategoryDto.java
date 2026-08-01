package com.assessment.shop_warehouse_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDto {
    private Long id;

    @NotBlank(message = "Nama kategori wajib diisi")
    private String nameCategory;

    private String description;
    private boolean isDeleted;
}
