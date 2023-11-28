package com.example.auth_service.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

// An interface that consists solely of constant definitions is a bad practice. The purpose of interfaces is to provide an API, not implementation details. That is, they should provide functions in the first place and constants only to assist these functions, for example, as possible arguments.
// Utility classes, which are collections of static members, are not meant to be instantiated. Even abstract utility classes, which can be extended, should not have public constructors.
@NoArgsConstructor(access = AccessLevel.NONE)
public final class ValidationRegex {
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d@$!%*#?&_]{8,}$";
    public static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
}
