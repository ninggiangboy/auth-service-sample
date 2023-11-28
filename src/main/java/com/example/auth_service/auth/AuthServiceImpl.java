package com.example.auth_service.auth;

import com.example.auth_service.jwt.JwtUtil;
import com.example.auth_service.jwt.Token;
import com.example.auth_service.jwt.TokenRepository;
import com.example.auth_service.jwt.TokenType;
import com.example.auth_service.user.User;
import com.example.auth_service.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()));
        String username = authRequest.getUsername();
        User user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(
                () -> new BadCredentialsException(String.format("User %s not found", username)));
        String jwtToken = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        jwtUtil.revokeAllUserTokens(user);
        jwtUtil.saveUserToken(user, refreshToken, TokenType.BEARER);
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        boolean isRevokedToken = tokenRepository.findByValue(refreshToken)
                .map(Token::isRevoked)
                .orElse(true);
        if (!jwtUtil.isTokenValid(refreshToken) || isRevokedToken) {
            throw new InvalidBearerTokenException("Invalid or revoked refresh token");
        }

        final String username = jwtUtil.extractUsername(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User %s not found", username)));
        String accessToken = jwtUtil.generateToken(user);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        String jwtToken = authHeader.substring(7);
        String username = jwtUtil.extractUsername(jwtToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User %s not found", username)));
        if (jwtUtil.isTokenValid(jwtToken)) {
            jwtUtil.revokeAllUserTokens(user);
        }
    }
}
