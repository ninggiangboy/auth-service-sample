package com.example.auth_service.validation;

import com.example.auth_service.constant.ValidationRegex;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isBlank()) {
            return true;
        }

        if (password.length() < 8) {
            addConstraintViolation(context, "Password must have at least 8 characters");
            return false;
        }

        if (!password.matches(ValidationRegex.PASSWORD_REGEX)) {
            addConstraintViolation(context, "Password must have at least one uppercase letter, one lowercase letter, and one number");
            return false;
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
