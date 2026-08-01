package com.assessment.shop_warehouse_api.controller;


import com.assessment.shop_warehouse_api.common.response.ApiResponse;
import com.assessment.shop_warehouse_api.dto.CategoryDto;
import com.assessment.shop_warehouse_api.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getAllCategory() {
        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    @GetMapping("/deleted")
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getAllDeleteCategory() {
        return ResponseEntity.ok(categoryService.getAllDeleteCategory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDto>> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        ApiResponse<CategoryDto> response = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> updateCategory(@PathVariable Long id,
                                                                   @Valid @RequestBody CategoryDto categoryDto) {
        ApiResponse<CategoryDto> response = categoryService.updateCategory(id, categoryDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Long id) {
        ApiResponse<String> response = categoryService.deleteCategory(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }




}
