package com.assessment.shop_warehouse_api.controller;

import com.assessment.shop_warehouse_api.common.response.ApiResponse;
import com.assessment.shop_warehouse_api.dto.ItemVariantDto;
import com.assessment.shop_warehouse_api.service.VariantService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VariantController {
    private final VariantService variantService;

    public VariantController(VariantService variantService){
        this.variantService = variantService;
    }

    @PostMapping("/api/items/{itemId}/variants")
    public ResponseEntity<ApiResponse<ItemVariantDto>> createVariant(@PathVariable Long itemId,
                                                                     @Valid @RequestBody ItemVariantDto dto){
        ApiResponse<ItemVariantDto> response = variantService.createVariant(itemId, dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/api/variants/{id}")
    public ResponseEntity<ApiResponse<ItemVariantDto>> updateVariant(@PathVariable Long id,
                                                                     @Valid @RequestBody ItemVariantDto dto){
        ApiResponse<ItemVariantDto> response = variantService.updateVariant(id, dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/api/variants/{id}")
    public ResponseEntity<ApiResponse<String>> deleteVariant(@PathVariable Long id){
        ApiResponse<String> response = variantService.deleteVariant(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/api/items/{itemId}/variants")
    public ResponseEntity<ApiResponse<List<ItemVariantDto>>> getVariantsByItemId(@PathVariable Long itemId){
        return ResponseEntity.ok(variantService.getVariantsByItemId(itemId));
    }

    @GetMapping("/api/variants/deleted")
    public ResponseEntity<ApiResponse<List<ItemVariantDto>>> getAllDeleteVariant(){
        return ResponseEntity.ok(variantService.getAllDeleteVariant());
    }

    @GetMapping("/api/variants")
    public ResponseEntity<ApiResponse<Page<ItemVariantDto>>> getAllVariant(
            @Parameter(description = "Search keyword to filter by item variant name (optional)")
            @RequestParam(required = false) String keyword,
            @ParameterObject Pageable pageable
            ){
       return ResponseEntity.ok(variantService.getAllItem(keyword, pageable));
    }

    @GetMapping("/api/variant/barcode/{barcode}")
    public ResponseEntity<ApiResponse<ItemVariantDto>> getVariantByBarcode(@PathVariable String barcode) {
        return ResponseEntity.ok(variantService.getItemByBarcode(barcode));
    }

}
