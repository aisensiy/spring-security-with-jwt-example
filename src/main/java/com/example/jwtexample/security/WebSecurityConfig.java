package com.example.jwtexample.security;

import com.example.jwtexample.User;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  private PasswordEncoder passwordEncoder;
  private JwtConfig jwtConfig;

  @Autowired
  public WebSecurityConfig(PasswordEncoder passwordEncoder,
      JwtConfig jwtConfig) {
    this.passwordEncoder = passwordEncoder;
    this.jwtConfig = jwtConfig;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilter(
            new JwtUsernameAndPasswordAuthenticationFilter(
                authenticationManager(), jwtConfig))
        .addFilterAfter(
            new JwtTokenVerifier(jwtConfig),
            JwtUsernameAndPasswordAuthenticationFilter.class)
        .authorizeRequests()
        .anyRequest().authenticated();
  }

  @Override
  @Bean
  protected UserDetailsService userDetailsService() {
    return new InMemoryUserDetailsManager(
        new User("test", passwordEncoder.encode("password"), Arrays.asList("NORMAL")));
  }
}
