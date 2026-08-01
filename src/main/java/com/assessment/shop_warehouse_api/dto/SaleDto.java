package com.assessment.shop_warehouse_api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SaleDto {
    private Long id;
    private String invoiceNumber;
    private String customerName;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;

    // Hanya diisi saat request pembuatan sale
    @NotEmpty(message = "Sale harus memiliki minimal 1 item")
    @Valid
    private List<SaleItemDto> items;
}
