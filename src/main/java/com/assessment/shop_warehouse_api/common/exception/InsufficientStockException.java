package com.assessment.shop_warehouse_api.common.exception;

public class InsufficientStockException extends RuntimeException{
    public InsufficientStockException(String message){
        super(message);
    }

}
