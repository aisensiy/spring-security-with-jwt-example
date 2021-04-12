package com.example.jwtexample.security;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UsernamePasswordRequest {
  private String username;
  private String password;
}
