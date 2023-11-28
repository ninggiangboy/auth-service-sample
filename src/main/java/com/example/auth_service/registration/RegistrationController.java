package com.example.auth_service.registration;

import com.example.auth_service.base.BaseController;
import com.example.auth_service.base.ResultResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/registration")
@RequiredArgsConstructor
public class RegistrationController extends BaseController {

    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<ResultResponse> register(@Valid @RequestBody RegistrationRequest registrationRequest)
            throws MethodArgumentNotValidException {
        registrationService.register(registrationRequest);
        return buildResponse("User registered successfully, please check your email to confirm");
    }

    @PostMapping("/confirm")
    public ResponseEntity<ResultResponse> confirmEmail(@RequestParam("token") String token) {
        registrationService.confirmEmail(token);
        return buildResponse("Email confirmed successfully");
    }
}
