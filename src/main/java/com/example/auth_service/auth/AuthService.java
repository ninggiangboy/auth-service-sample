package com.example.auth_service.auth;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    AuthResponse authenticate(AuthRequest authRequest);

    AuthResponse refreshToken(String refreshToken);

    void logout(HttpServletRequest request);
}
