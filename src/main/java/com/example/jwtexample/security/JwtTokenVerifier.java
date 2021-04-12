package com.example.jwtexample.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtTokenVerifier extends OncePerRequestFilter {

  private JwtConfig jwtConfig;

  public JwtTokenVerifier(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
  }

  public boolean isNullOrEmpty(String str) {
    return str == null || str.isEmpty();
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authorizationHeader = request.getHeader("Authorization");

    if (isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = authorizationHeader.replace("Bearer ", "");

    try {
      Jws<Claims> claimsJws =
          Jwts.parserBuilder()
              .setSigningKey(jwtConfig.getSecretKey())
              .build()
              .parseClaimsJws(token);
      Claims body = claimsJws.getBody();
      String username = body.getSubject();
      List<String> authorities = (List<String>) body.get("authorities");
      Set<SimpleGrantedAuthority> simpleGrantedAuthorities =
          authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
      Authentication authentication =
          new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (JwtException e) {
      throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
    }

    filterChain.doFilter(request, response);
  }
}
