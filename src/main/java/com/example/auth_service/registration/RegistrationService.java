package com.example.auth_service.registration;

import org.springframework.web.bind.MethodArgumentNotValidException;

public interface RegistrationService {
    void register(RegistrationRequest registrationRequest) throws MethodArgumentNotValidException;

    void confirmEmail(String token);
}
