package com.assessment.shop_warehouse_api.service.impl;

import com.assessment.shop_warehouse_api.common.exception.InsufficientStockException;
import com.assessment.shop_warehouse_api.common.exception.InternalServerException;
import com.assessment.shop_warehouse_api.common.exception.ResourceNotFoundException;
import com.assessment.shop_warehouse_api.common.response.ApiResponse;
import com.assessment.shop_warehouse_api.dto.SaleDto;
import com.assessment.shop_warehouse_api.dto.SaleItemDto;
import com.assessment.shop_warehouse_api.entity.*;
import com.assessment.shop_warehouse_api.repository.ItemVariantRepository;
import com.assessment.shop_warehouse_api.repository.SaleRepository;
import com.assessment.shop_warehouse_api.repository.StockMovementRepository;
import com.assessment.shop_warehouse_api.service.SalesService;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesServiceImpl implements SalesService {

    private final SaleRepository saleRepository;
    private final ItemVariantRepository variantRepository;
    private final StockMovementRepository stockMovementRepository;

    public SalesServiceImpl(SaleRepository saleRepository,
                            ItemVariantRepository variantRepository,
                            StockMovementRepository stockMovementRepository) {
        this.saleRepository = saleRepository;
        this.variantRepository = variantRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    @Override
    @Transactional
    public ApiResponse<SaleDto> createSale(SaleDto saleDto){
        try{
            ApiResponse<SaleDto>response = new ApiResponse<>();
            Sale sale = new Sale();
            sale.setInvoiceNumber(generateInvoiceNumber());
            sale.setCustomerName(saleDto.getCustomerName());

            BigDecimal total = BigDecimal.ZERO;

            for (SaleItemDto itemRequest : saleDto.getItems()){
                ItemVariant variant = variantRepository.findById(itemRequest.getVariantId())
                        .orElseThrow(()-> new ResourceNotFoundException("Variant", "id", itemRequest.getVariantId()));

                if (variant.getStock() < itemRequest.getQuantity()){
                    throw new InsufficientStockException("Insufficient stock for variant " + variant.getVariantName());
                }

                int previousStock = variant.getStock();
                int newStock = previousStock - itemRequest.getQuantity();
                variant.setStock(newStock);
                variantRepository.save(variant);

                BigDecimal subtotal = variant.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
                total = total.add(subtotal);

                SaleDetail detail = new SaleDetail();
                detail.setSale(sale);
                detail.setVariant(variant);
                detail.setQuantity(itemRequest.getQuantity());
                detail.setPrice(variant.getPrice());
                detail.setSubtotal(subtotal);
                sale.getDetails().add(detail);

                StockMovement movement = new StockMovement();
                movement.setVariant(variant);
                movement.setMovementType(MovementType.SALE);
                movement.setQuantity(itemRequest.getQuantity());
                movement.setPreviousStock(previousStock);
                movement.setCurrentStock(newStock);
                movement.setReferenceNumber(sale.getInvoiceNumber());
                stockMovementRepository.save(movement);
            }

            sale.setTotalAmount(total);
            sale = saleRepository.save(sale);

            response.setStatus(true);
            response.getMessages().add("Sale completed successfully");
            response.setData(mapToDto(sale));
            return response;
        } catch (ResourceNotFoundException | InsufficientStockException | OptimisticLockingFailureException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalServerException("Failed to process sale: " + ex.getMessage(), ex);
        }
    }

    @Override
    public ApiResponse<List<SaleDto>> getAllSale(){
        try{
            ApiResponse<List<SaleDto>> response = new ApiResponse<>();
            List<SaleDto> dtos = saleRepository.findAll().stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            response.setStatus(true);
            response.getMessages().add("Sales retrived successfully");
            response.setData(dtos);
            return response;
        } catch (Exception ex) {
            throw new InternalServerException("Failed to retrieve sales: " + ex.getMessage(), ex);
        }
    }

    @Override
    public ApiResponse<SaleDto> getSaleById(Long id){
        try{
            ApiResponse<SaleDto> response = new ApiResponse<>();

            Sale sale = saleRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Sale", "id", id));

            response.setStatus(true);
            response.getMessages().add("Sale retrived successfully");
            response.setData(mapToDto(sale));
            return response;
        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalServerException("Failed to retrieve sale: " + ex.getMessage(), ex);
        }
    }

    private String generateInvoiceNumber() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long countToday = saleRepository.count() + 1;
        return String.format("INV-%s%04d", datePart, countToday);
    }

    private SaleDto mapToDto(Sale sale) {
        SaleDto dto = new SaleDto();
        dto.setId(sale.getId());
        dto.setInvoiceNumber(sale.getInvoiceNumber());
        dto.setCustomerName(sale.getCustomerName());
        dto.setTotalAmount(sale.getTotalAmount());
        dto.setCreatedAt(sale.getCreatedAt());
        return dto;
    }

}
