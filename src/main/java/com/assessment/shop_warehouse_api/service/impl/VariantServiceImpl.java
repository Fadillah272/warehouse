package com.assessment.shop_warehouse_api.service.impl;

import com.assessment.shop_warehouse_api.common.exception.DuplicateResourceException;
import com.assessment.shop_warehouse_api.common.exception.InternalServerException;
import com.assessment.shop_warehouse_api.common.exception.ResourceNotFoundException;
import com.assessment.shop_warehouse_api.common.response.ApiResponse;
import com.assessment.shop_warehouse_api.dto.ItemVariantDto;
import com.assessment.shop_warehouse_api.entity.Item;
import com.assessment.shop_warehouse_api.entity.ItemVariant;
import com.assessment.shop_warehouse_api.repository.ItemRepository;
import com.assessment.shop_warehouse_api.repository.ItemVariantRepository;
import com.assessment.shop_warehouse_api.service.VariantService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VariantServiceImpl implements VariantService {
    private final ItemVariantRepository variantRepository;
    private final ItemRepository itemRepository;

    public VariantServiceImpl(ItemVariantRepository variantRepository, ItemRepository itemRepository){
        this.variantRepository = variantRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional
    public ApiResponse<ItemVariantDto> createVariant(Long itemId, ItemVariantDto dto){
        try{

            ApiResponse<ItemVariantDto> response = new ApiResponse<>();

            if (variantRepository.existsByBarcode(dto.getBarcode())){
                throw new DuplicateResourceException("Barcode already exists: " + dto.getBarcode());
            }

            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
            ItemVariant variant = new ItemVariant();
            variant.setItem(item);
            variant.setVariantName(dto.getVariantName());
            variant.setColor(dto.getColor());
            variant.setSize(dto.getSize());
            variant.setBarcode(dto.getBarcode());
            variant.setPrice(dto.getPrice());
            variant.setStock(dto.getStock());
            variant = variantRepository.save(variant);

            response.setStatus(true);
            response.getMessages().add("Variant created successfully");
            response.setData(mapToDto(variant));
            return response;
        } catch (DuplicateResourceException | ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalServerException("Failed to create variant: " + ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional
    public ApiResponse<ItemVariantDto> updateVariant(Long id, ItemVariantDto dto){
        try {
            ApiResponse<ItemVariantDto> response = new ApiResponse<>();

            ItemVariant variant = variantRepository.findById(id)
                    .orElseThrow(()-> new ResourceNotFoundException("Variant", "id", id));
            Item item = itemRepository.findById(dto.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Item", "id", dto.getItemId()));
            variant.setVariantName(dto.getVariantName());
            variant.setItem(item);
            variant.setColor(dto.getColor());
            variant.setSize(dto.getSize());
            variant.setBarcode(dto.getBarcode());
            variant.setPrice(dto.getPrice());
            variant = variantRepository.save(variant);

            response.setStatus(true);
            response.getMessages().add("Variant updated successfully");
            response.setData(mapToDto(variant));
            return response;

        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalServerException("Failed to update variant: " + ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional
    public ApiResponse<String> deleteVariant(Long id){
        try{
            ApiResponse<String> response = new ApiResponse<>();

            ItemVariant variant = variantRepository.findById(id)
                    .orElseThrow(()-> new ResourceNotFoundException("Variant", "id", id));

            variant.setDeleted(true);
            variantRepository.save(variant);

            response.setStatus(true);
            response.getMessages().add("Variant marked as deleted successfully");
            response.setData("Variant with ID " + id + " is deleted");
            return response;
        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalServerException("Failed to delete variant: " + ex.getMessage(), ex);
        }
    }

    @Override
    public ApiResponse<List<ItemVariantDto>> getVariantsByItemId(Long itemId){
        try{
            ApiResponse<List<ItemVariantDto>> response = new ApiResponse<>();
            List<ItemVariantDto> dtos = variantRepository.findByItemIdAndIsDeletedFalse(itemId).stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());

            response.setStatus(true);
            response.getMessages().add("Variant retrieved successfully");
            response.setData(dtos);
            return response;
        } catch (Exception ex) {
            throw new InternalServerException("Failed to retrieve variants: " + ex.getMessage(), ex);
        }
    }

    @Override
    public ApiResponse<List<ItemVariantDto>> getAllDeleteVariant(){
        try{
            ApiResponse<List<ItemVariantDto>> response = new ApiResponse<>();

            List<ItemVariantDto> dtos = variantRepository.findAllByIsDeletedTrue().stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            response.setStatus(true);
            response.getMessages().add("Deleted variants retrieved successfully");
            response.setData(dtos);
            return response;
        } catch (Exception ex) {
            throw new InternalServerException("Failed to retrieve deleted variants: " + ex.getMessage(), ex);
        }
    }

    @Override
    public ApiResponse<Page<ItemVariantDto>> getAllItem(String keyword, Pageable pageable){
        try{
            ApiResponse<org.springframework.data.domain.Page<ItemVariantDto>> response = new ApiResponse<>();
            Page<ItemVariant> itemVariants = StringUtils.hasText(keyword)
                    ? variantRepository.findByIsDeletedFalseAndVariantNameContainingIgnoreCase(keyword, pageable)
                    : variantRepository.findByIsDeletedFalse(pageable);
            response.setStatus(true);
            response.getMessages().add("Variants retrieved successfully");
            response.setData(itemVariants.map(this::mapToDto));
            return response;
        } catch (Exception ex) {
            throw new InternalServerException("Failed to retrieve items: " + ex.getMessage(), ex);
        }
    }

    @Override
    public ApiResponse<ItemVariantDto> getItemByBarcode(String barcode){
        try{
            ApiResponse<ItemVariantDto> response = new ApiResponse<>();
            ItemVariant itemVariant = variantRepository.findAllByBarcode(barcode).stream()
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Variant", "barcode", barcode));

            response.setStatus(true);
            response.getMessages().add("Variants retrieved successfully");
            response.setData(mapToDto(itemVariant));
            return response;
        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalServerException("Failed to retrieve item: " + ex.getMessage(), ex);
        }
    }





    private ItemVariantDto mapToDto(ItemVariant variant) {
        ItemVariantDto dto = new ItemVariantDto();
        dto.setId(variant.getId());
        dto.setItemId(variant.getItem().getId());
        dto.setVariantName(variant.getVariantName());
        dto.setColor(variant.getColor());
        dto.setSize(variant.getSize());
        dto.setBarcode(variant.getBarcode());
        dto.setPrice(variant.getPrice());
        dto.setStock(variant.getStock());
        dto.setDeleted(variant.isDeleted());
        return dto;
    }
}
