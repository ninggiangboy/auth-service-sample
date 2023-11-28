package com.example.auth_service.user;

import com.example.auth_service.registration.RegistrationRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;

public interface UserService {
    User createUser(RegistrationRequest registrationRequest) throws MethodArgumentNotValidException;
}
