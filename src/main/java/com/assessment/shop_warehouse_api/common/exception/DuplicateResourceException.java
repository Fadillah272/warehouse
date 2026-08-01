package com.assessment.shop_warehouse_api.common.exception;

public class DuplicateResourceException extends RuntimeException{
    public DuplicateResourceException(String message){
        super(message);
    }

}
