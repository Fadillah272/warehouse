package com.assessment.shop_warehouse_api.controller;

import com.assessment.shop_warehouse_api.common.response.ApiResponse;
import com.assessment.shop_warehouse_api.dto.SaleDto;
import com.assessment.shop_warehouse_api.service.SalesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesController {
    private final SalesService salesService;
    public SalesController(SalesService salesService){
        this.salesService = salesService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SaleDto>> createSale(@Valid @RequestBody SaleDto saleDto) {
        ApiResponse<SaleDto> response = salesService.createSale(saleDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SaleDto>>> getAllSale(){
        return ResponseEntity.ok(salesService.getAllSale());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SaleDto>> getSaleById(@PathVariable Long id){
        return ResponseEntity.ok(salesService.getSaleById(id));
    }
}
