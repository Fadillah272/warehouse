package com.assessment.shop_warehouse_api.controller;


import com.assessment.shop_warehouse_api.common.response.ApiResponse;
import com.assessment.shop_warehouse_api.dto.ItemDto;
import com.assessment.shop_warehouse_api.service.ItemService;
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
@RequestMapping("/api/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService){
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ItemDto>> create(@Valid @RequestBody ItemDto itemDto) {
        ApiResponse<ItemDto> response = itemService.createItem(itemDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemDto>> updateItem(@PathVariable Long id, @Valid @RequestBody ItemDto itemDto){
        ApiResponse<ItemDto> response = itemService.updateItem(id, itemDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteItem(@PathVariable Long id){
        ApiResponse<String> response = itemService.deleteItem(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ItemDto>>> getAllItem(
            @Parameter(description = "Search keyword to filter by item name (optional)")
            @RequestParam(required = false) String keyword,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(itemService.getAllItem(keyword, pageable));
    }

    @GetMapping("/deleted")
    public ResponseEntity<ApiResponse<List<ItemDto>>> getAllDeleteItem(){
        return ResponseEntity.ok(itemService.getAllDeleteItem());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemDto>> getItemById(@PathVariable Long id){
        return ResponseEntity.ok(itemService.getItemById(id));
    }

}
