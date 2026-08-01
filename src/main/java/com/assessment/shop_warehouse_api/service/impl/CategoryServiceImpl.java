package com.assessment.shop_warehouse_api.service.impl;

import com.assessment.shop_warehouse_api.common.exception.InternalServerException;
import com.assessment.shop_warehouse_api.common.exception.ResourceNotFoundException;
import com.assessment.shop_warehouse_api.common.response.ApiResponse;
import com.assessment.shop_warehouse_api.dto.CategoryDto;
import com.assessment.shop_warehouse_api.entity.Category;
import com.assessment.shop_warehouse_api.repository.CategoryRepository;
import com.assessment.shop_warehouse_api.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public ApiResponse<CategoryDto> createCategory(CategoryDto categoryDto) {
        try {
            ApiResponse<CategoryDto> response = new ApiResponse<>();

            Category category = mapToEntity(categoryDto);
            category = categoryRepository.save(category);

            response.setStatus(true);
            response.getMessages().add("Category created successfully");
            response.setData(mapToDto(category));
            return response;
        } catch (Exception ex) {
            throw new InternalServerException("Failed to create category: " + ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional
    public ApiResponse<CategoryDto> updateCategory(Long id, CategoryDto categoryDto) {
        try {
            ApiResponse<CategoryDto> response = new ApiResponse<>();

            Category existingCategory = categoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

            existingCategory.setNameCategory(categoryDto.getNameCategory());
            existingCategory.setDescription(categoryDto.getDescription());
            existingCategory = categoryRepository.save(existingCategory);

            response.setStatus(true);
            response.getMessages().add("Category updated successfully");
            response.setData(mapToDto(existingCategory));
            return response;
        } catch (ResourceNotFoundException ex) {
            // Business exception yang sudah jelas → teruskan apa adanya
            // supaya ditangani oleh handler spesifiknya di GlobalExceptionHandler.
            throw ex;
        } catch (Exception ex) {
            throw new InternalServerException("Failed to update category: " + ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional
    public ApiResponse<String> deleteCategory(Long id) {
        try {
            ApiResponse<String> response = new ApiResponse<>();

            Category existingCategory = categoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

            existingCategory.setDeleted(true);
            categoryRepository.save(existingCategory);

            response.setStatus(true);
            response.getMessages().add("Category marked as deleted successfully");
            response.setData("Category with ID " + id + " is deleted");
            return response;
        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalServerException("Failed to delete category: " + ex.getMessage(), ex);
        }
    }

    @Override
    public ApiResponse<List<CategoryDto>> getAllCategory() {
        try {
            ApiResponse<List<CategoryDto>> response = new ApiResponse<>();

            List<CategoryDto> categoryDtos = categoryRepository.findAllByIsDeletedFalse().stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());

            response.setStatus(true);
            response.getMessages().add("Categories retrieved successfully");
            response.setData(categoryDtos);
            return response;
        } catch (Exception ex) {
            throw new InternalServerException("Failed to retrieve categories: " + ex.getMessage(), ex);
        }
    }

    @Override
    public ApiResponse<List<CategoryDto>> getAllDeleteCategory() {
        try {
            ApiResponse<List<CategoryDto>> response = new ApiResponse<>();

            List<CategoryDto> categoryDtos = categoryRepository.findAllByIsDeletedTrue().stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());

            response.setStatus(true);
            response.getMessages().add("Deleted categories retrieved successfully");
            response.setData(categoryDtos);
            return response;
        } catch (Exception ex) {
            throw new InternalServerException("Failed to retrieve deleted categories: " + ex.getMessage(), ex);
        }
    }

    @Override
    public ApiResponse<CategoryDto> getCategoryById(Long id) {
        try {
            ApiResponse<CategoryDto> response = new ApiResponse<>();

            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

            response.setStatus(true);
            response.getMessages().add("Category retrieved successfully");
            response.setData(mapToDto(category));
            return response;
        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalServerException("Failed to retrieve category: " + ex.getMessage(), ex);
        }
    }

    private CategoryDto mapToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setNameCategory(category.getNameCategory());
        dto.setDescription(category.getDescription());
        dto.setDeleted(category.isDeleted());
        return dto;
    }

    private Category mapToEntity(CategoryDto dto) {
        Category category = new Category();
        category.setNameCategory(dto.getNameCategory());
        category.setDescription(dto.getDescription());
        return category;
    }
}
