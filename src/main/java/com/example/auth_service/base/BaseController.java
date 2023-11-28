package com.example.auth_service.base;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RequiredArgsConstructor
public class BaseController {
    protected ResponseEntity<ResultResponse> buildResponse(String message, Object result, HttpStatus status) {
        return ResponseEntity.status(status).body(
                ResultResponse.builder()
                        .result(result)
                        .message(message)
                        .status(status.value())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    protected ResponseEntity<ResultResponse> buildResponse(String message, Object result) {
        return buildResponse(message, result, HttpStatus.OK);
    }

    protected ResponseEntity<ResultResponse> buildResponse(String message, HttpStatus status) {
        return buildResponse(message, new ArrayList<>(), status);
    }

    protected ResponseEntity<ResultResponse> buildResponse(String message) {
        return buildResponse(message, new ArrayList<>(), HttpStatus.OK);
    }

    protected ResponseEntity<ResultResponse> buildResponse() {
        return buildResponse("", new ArrayList<>(), HttpStatus.OK);
    }
}
