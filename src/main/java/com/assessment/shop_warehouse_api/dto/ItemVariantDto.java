package com.assessment.shop_warehouse_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemVariantDto {
    private Long id;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long itemId;

    @NotBlank(message = "Nama varian wajib diisi")
    private String variantName;

    private String color;
    private String size;

    @NotBlank(message = "Barcode wajib diisi")
    private String barcode;

    @DecimalMin(value = "0.01", message = "Harga harus lebih besar dari 0")
    private BigDecimal price;

    @Min(value = 0, message = "Stok tidak boleh negatif")
    private Integer stock;

    private boolean isDeleted;
}
