package com.assessment.shop_warehouse_api.common.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ApiResponse<T> {

    private boolean status;
    private List<String> messages = new ArrayList<>();
    private T data;

    public ApiResponse() {
    }

    // Helper untuk kasus sukses satu pesan
    public static <T> ApiResponse<T> success(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(true);
        response.getMessages().add(message);
        response.setData(data);
        return response;
    }

    // Helper untuk kasus gagal
    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(false);
        response.getMessages().add(message);
        return response;
    }
}
