package com.assessment.shop_warehouse_api.service.impl;

import com.assessment.shop_warehouse_api.common.exception.InsufficientStockException;
import com.assessment.shop_warehouse_api.common.exception.InternalServerException;
import com.assessment.shop_warehouse_api.common.exception.ResourceNotFoundException;
import com.assessment.shop_warehouse_api.common.response.ApiResponse;
import com.assessment.shop_warehouse_api.dto.StockAdjustmentDto;
import com.assessment.shop_warehouse_api.dto.StockDto;
import com.assessment.shop_warehouse_api.entity.Item;
import com.assessment.shop_warehouse_api.entity.ItemVariant;
import com.assessment.shop_warehouse_api.entity.MovementType;
import com.assessment.shop_warehouse_api.entity.StockMovement;
import com.assessment.shop_warehouse_api.repository.ItemVariantRepository;
import com.assessment.shop_warehouse_api.repository.StockMovementRepository;
import com.assessment.shop_warehouse_api.service.StockService;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockServiceImpl implements StockService {

    private final ItemVariantRepository itemVariantRepository;
    private final StockMovementRepository stockMovementRepository;

    public StockServiceImpl(ItemVariantRepository variantRepository, StockMovementRepository stockMovementRepository){
        this.itemVariantRepository = variantRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    @Override
    public ApiResponse<List<StockDto>> getAllStock(){
        try{
            ApiResponse<List<StockDto>> response = new ApiResponse<>();
            List<StockDto> dtos = itemVariantRepository.findAllByIsDeletedFalse().stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());

            response.setStatus(true);
            response.getMessages().add("Stocks retrived successfully");
            response.setData(dtos);
            return response;

        } catch (Exception ex) {
            throw new InternalServerException("Failed to retrieve stocks: " + ex.getMessage(), ex);
        }
    }

    @Override
    public ApiResponse<List<StockDto>> getLowStock(int threshold){
        try{
            ApiResponse<List<StockDto>> response = new ApiResponse<>();

            List<StockDto> dtos = itemVariantRepository.findByStockLessThanEqualAndIsDeletedFalse(threshold).stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());

            response.setStatus(true);
            response.getMessages().add("Low stock items retrived successfully");
            response.setData(dtos);
            return response;
        } catch (Exception ex) {
            throw new InternalServerException("Failed to retrieve low stock items: " + ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional
    public ApiResponse<StockDto> adjustStock(StockAdjustmentDto dto){
        try{
            ApiResponse<StockDto> response = new ApiResponse<>();
            ItemVariant variant = itemVariantRepository.findById(dto.getVariantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Variant", "id", dto.getVariantId()));

            int previousStock = variant.getStock();
            int newStock = previousStock + dto.getAdjustment();

            if (newStock < 0) {
                throw new InsufficientStockException(
                        "Adjustment would result in negative stock for variant: " + variant.getVariantName()
                );
            }

            variant.setStock(newStock);
            itemVariantRepository.save(variant);

            StockMovement movement = new StockMovement();
            movement.setVariant(variant);
            movement.setMovementType(MovementType.ADJUSTMENT);
            movement.setQuantity(dto.getAdjustment());
            movement.setPreviousStock(previousStock);
            movement.setCurrentStock(newStock);
            movement.setReferenceNumber(dto.getReferenceNumber());
            stockMovementRepository.save(movement);

            response.setStatus(true);
            response.getMessages().add("Stock adjusted successfully");
            response.setData(mapToDto(variant));
            return response;

        } catch (ResourceNotFoundException | InsufficientStockException | OptimisticLockingFailureException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalServerException("Failed to adjust stock: " + ex.getMessage(), ex);
        }
    }



    private StockDto mapToDto(ItemVariant variant) {
        return new StockDto(variant.getId(), variant.getVariantName(), variant.getBarcode(), variant.getStock());
    }


}
