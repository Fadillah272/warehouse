package com.assessment.shop_warehouse_api.service.impl;

import com.assessment.shop_warehouse_api.common.response.ApiResponse;
import com.assessment.shop_warehouse_api.dto.DashboardDto;
import com.assessment.shop_warehouse_api.repository.ItemRepository;
import com.assessment.shop_warehouse_api.repository.ItemVariantRepository;
import com.assessment.shop_warehouse_api.repository.SaleRepository;
import com.assessment.shop_warehouse_api.service.DashboardService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class DashboardServiceImpl implements DashboardService {
    private static final int LOW_STOCK_THRESHOLD = 10;

    private final ItemRepository itemRepository;
    private final ItemVariantRepository variantRepository;
    private final SaleRepository saleRepository;

    public DashboardServiceImpl(ItemRepository itemRepository,
                                ItemVariantRepository itemVariantRepository,
                                SaleRepository saleRepository){
        this.itemRepository = itemRepository;
        this.variantRepository = itemVariantRepository;
        this.saleRepository = saleRepository;
    }

    @Override
    public ApiResponse<DashboardDto> getSummary(){
        ApiResponse<DashboardDto> response = new ApiResponse<>();
        long totalItems = itemRepository.findByIsDeletedFalse(org.springframework.data.domain.Pageable.unpaged()).getTotalElements();
        long totalVariants = variantRepository.findAllByIsDeletedFalse().size();
        long lowStock = variantRepository.findByStockLessThanEqualAndIsDeletedFalse(LOW_STOCK_THRESHOLD).size();

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23,59,59);
        long todaySales = saleRepository.countByCreatedAtBetween(startOfDay, endOfDay);

        response.setStatus(true);
        response.getMessages().add("Dashboard summary retrieved successfully");
        response.setData(new DashboardDto(totalItems, totalVariants, lowStock, todaySales));
        return response;
    }
}
