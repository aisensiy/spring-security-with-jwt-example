package com.example.jwtexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class JwtExampleApplication {

  public static void main(String[] args) {
    SpringApplication.run(JwtExampleApplication.class, args);
  }
}
