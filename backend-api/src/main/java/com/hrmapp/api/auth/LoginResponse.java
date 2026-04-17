package com.hrmapp.api.auth;

public record LoginResponse(
        Long userId,
        Long employeeId,
        String username,
        String roleCode,
        String accessToken,
        String refreshToken
) {
}