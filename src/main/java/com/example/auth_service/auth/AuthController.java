package com.example.auth_service.auth;

import com.example.auth_service.base.BaseController;
import com.example.auth_service.base.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final AuthService authService;

    @PostMapping("/authenticate")
    public ResponseEntity<ResultResponse> authenticate(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authService.authenticate(authRequest);
        return buildResponse("Login successfully", authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResultResponse> logout(HttpServletRequest request) {
        authService.logout(request);
        return buildResponse("Logout successfully");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ResultResponse> refreshToken(@NotBlank @RequestParam String refreshToken) {
        AuthResponse authResponse = authService.refreshToken(refreshToken);
        return buildResponse("Refresh token successfully", authResponse);
    }
}
