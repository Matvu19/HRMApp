package com.hrmapp.api.common;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data
) {
}