package com.assessment.shop_warehouse_api.common.exception;

// Dilempar saat terjadi error tak terduga (mis. kegagalan akses database,
// null pointer, dsb) di dalam service layer. Pesan asli dicatat lewat cause,
// tapi pesan yang dikembalikan ke client dibuat aman/tidak membocorkan detail teknis.
public class InternalServerException extends RuntimeException {
    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
