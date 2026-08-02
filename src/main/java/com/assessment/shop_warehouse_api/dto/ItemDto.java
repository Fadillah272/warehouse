package com.assessment.shop_warehouse_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long categoryId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String categoryName;
    private boolean isDeleted;
}
