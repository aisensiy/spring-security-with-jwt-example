package com.example.jwtexample.security;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@NoArgsConstructor
@Data
public class JwtConfig {
  private String secret;
  private Integer days;

  public SecretKey getSecretKey() {
    return Keys.hmacShaKeyFor(secret.getBytes());
  }
}
