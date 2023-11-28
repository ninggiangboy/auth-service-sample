package com.example.auth_service.registration;

import com.example.auth_service.email.EmailService;
import com.example.auth_service.exception.ExpiredException;
import com.example.auth_service.jwt.JwtUtil;
import com.example.auth_service.jwt.Token;
import com.example.auth_service.jwt.TokenRepository;
import com.example.auth_service.jwt.TokenType;
import com.example.auth_service.user.User;
import com.example.auth_service.user.UserRepository;
import com.example.auth_service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    @Override
    public void register(RegistrationRequest registrationRequest) throws MethodArgumentNotValidException {
        User user = userService.createUser(registrationRequest);
        String confirmToken = jwtUtil.generateConfirmToken(user);
        jwtUtil.saveUserToken(user, confirmToken, TokenType.CONFIRM);

        String subject = "Confirm email";
        emailService.sendEmail(subject, confirmToken, user.getEmail());
    }

    @Override
    public void confirmEmail(String confirmToken) {
        Token token = tokenRepository.findByValue(confirmToken)
                .orElseThrow(() -> new ExpiredException("Token not found"));
        User user = token.getUser();
        if (Boolean.TRUE.equals(user.getIsEnabled()) || token.isRevoked()) {
            throw new ExpiredException("Email already confirmed");
        }
        if (jwtUtil.isTokenValid(confirmToken)) {
            user.setIsEnabled(true);
            userRepository.save(user);
        } else {
            throw new ExpiredException("The confirmation email has expired");
        }
        jwtUtil.revokeAllUserTokens(user);
    }
}
