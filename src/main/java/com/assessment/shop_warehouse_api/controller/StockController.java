package com.assessment.shop_warehouse_api.controller;

import com.assessment.shop_warehouse_api.common.response.ApiResponse;
import com.assessment.shop_warehouse_api.dto.StockAdjustmentDto;
import com.assessment.shop_warehouse_api.dto.StockDto;
import com.assessment.shop_warehouse_api.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService){
        this.stockService = stockService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StockDto>>> getAllStock(){
        return ResponseEntity.ok(stockService.getAllStock());
    }

    @GetMapping("/low")
    public ResponseEntity<ApiResponse<List<StockDto>>> getLowStock(@RequestParam(defaultValue = "10") int threshold){
        return ResponseEntity.ok(stockService.getLowStock(threshold));
    }

    @PostMapping("/adjustment")
    public ResponseEntity<ApiResponse<StockDto>> adjustStock(@Valid @RequestBody StockAdjustmentDto dto) {
        return ResponseEntity.ok(stockService.adjustStock(dto));
    }
}
