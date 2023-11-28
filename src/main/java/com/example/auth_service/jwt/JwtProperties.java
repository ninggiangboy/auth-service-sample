package com.example.auth_service.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {
    private String secretKey;
    private long accessExpiration;
    private long refreshExpiration;
    private long confirmExpiration;
}
