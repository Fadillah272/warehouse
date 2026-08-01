package com.assessment.shop_warehouse_api.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.assessment.shop_warehouse_api.common.exception.DuplicateResourceException;
import com.assessment.shop_warehouse_api.common.exception.InternalServerException;
import com.assessment.shop_warehouse_api.common.exception.ResourceNotFoundException;
import com.assessment.shop_warehouse_api.common.response.ApiResponse;
import com.assessment.shop_warehouse_api.dto.ItemDto;
import com.assessment.shop_warehouse_api.entity.Category;
import com.assessment.shop_warehouse_api.entity.Item;
import com.assessment.shop_warehouse_api.repository.CategoryRepository;
import com.assessment.shop_warehouse_api.repository.ItemRepository;
import com.assessment.shop_warehouse_api.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    public ItemServiceImpl(ItemRepository itemRepository, CategoryRepository categoryRepository){
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public ApiResponse<ItemDto> createItem(ItemDto itemDto){
        try{
            ApiResponse<ItemDto> response = new ApiResponse<>();
            if (itemRepository.existsBySku(itemDto.getSku())){
                throw new DuplicateResourceException("SKU already exists: " + itemDto.getSku());
            }

            Category category = categoryRepository.findById(itemDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", itemDto.getCategoryId()));
            Item item = new Item();
            item.setSku(itemDto.getSku());
            item.setNameItem(itemDto.getNameItem());
            item.setDescription(itemDto.getDescription());
            item.setCategory(category);
            item = itemRepository.save(item);

            response.setStatus(true);
            response.getMessages().add("Item created successfully");
            response.setData(mapToDto(item));
            return response;

        }catch (DuplicateResourceException | ResourceNotFoundException ex) {
            throw ex;
        }catch (Exception ex){
            throw new InternalServerException("Failed to create item: " + ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional
    public ApiResponse<ItemDto> updateItem(Long id, ItemDto itemDto){
        try{
            ApiResponse<ItemDto> response = new ApiResponse<>();
            Item item = itemRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Item", "id", id));

            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", itemDto.getCategoryId()));
            item.setSku(itemDto.getSku());
            item.setNameItem(itemDto.getNameItem());
            item.setDescription(itemDto.getDescription());
            item.setCategory(category);
            item = itemRepository.save(item);

            response.setStatus(true);
            response.getMessages().add("Item updated successfully");
            response.setData(mapToDto(item));
            return response;

        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalServerException("Failed to update item: " + ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional
    public ApiResponse<String> deleteItem(Long id){
        try{
            ApiResponse<String> response = new ApiResponse<>();
            Item item = itemRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Item", "id", id));

            item.setDeleted(true);
            itemRepository.save(item);

            response.setStatus(true);
            response.getMessages().add("Item marked as deleted successfully");
            response.setData("Item with ID " + id + " is deleted");
            return response;

        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalServerException("Failed to delete item: " + ex.getMessage(), ex);
        }
    }

    @Override
    public ApiResponse<Page<ItemDto>> getAllItem(String keyword, Pageable pageable) {
        try {
            ApiResponse<Page<ItemDto>> response = new ApiResponse<>();

            Page<Item> items = StringUtils.hasText(keyword)
                    ? itemRepository.findByIsDeletedFalseAndNameItemContainingIgnoreCase(keyword, pageable)
                    : itemRepository.findByIsDeletedFalse(pageable);

            response.setStatus(true);
            response.getMessages().add("Items retrieved successfully");
            response.setData(items.map(this::mapToDto));
            return response;
        } catch (Exception ex) {
            throw new InternalServerException("Failed to retrieve items: " + ex.getMessage(), ex);
        }
    }

    @Override
    public ApiResponse<List<ItemDto>> getAllDeleteItem(){
        try{
            ApiResponse<List<ItemDto>> response = new ApiResponse<>();
            List<ItemDto> itemDtos = itemRepository.findAllByIsDeletedTrue().stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());

            response.setStatus(true);
            response.getMessages().add("Deleted items retrieved successfully");
            response.setData(itemDtos);
            return response;

        } catch (Exception ex) {
            throw new InternalServerException("Failed to retrieve deleted items: " + ex.getMessage(), ex);
        }
    }

    @Override
    public ApiResponse<ItemDto> getItemById(Long id){
        try {
            ApiResponse<ItemDto> response = new ApiResponse<>();
            Item item = itemRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Item", "id", id));
            response.setStatus(true);
            response.getMessages().add("Item retrieved successfully");
            response.setData(mapToDto(item));
            return response;
        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalServerException("Failed to retrieve item: " + ex.getMessage(), ex);
        }
    }





    private ItemDto mapToDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setSku(item.getSku());
        dto.setNameItem(item.getNameItem());
        dto.setDescription(item.getDescription());
        dto.setCategoryId(item.getCategory().getId());
        dto.setCategoryName(item.getCategory().getNameCategory());
        dto.setDeleted(item.isDeleted());
        return dto;
    }
}
