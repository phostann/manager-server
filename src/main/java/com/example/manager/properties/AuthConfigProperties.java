package com.example.manager.properties;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.crypto.SecretKey;
import java.util.Date;

@Data
@ConfigurationProperties(prefix = "auth")
public class AuthConfigProperties {

    private String secret;

    private long expiration;

    private long refreshExpiration;

    private String tokenPrefix;

    private String JwtAuthHeader;

    private String ApiKeyAuthHeader;

    // white list
    private String[] whiteList;

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public Date getExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    public Date getRefreshExpirationDate() {
        return new Date(System.currentTimeMillis() + refreshExpiration * 1000);
    }
}

