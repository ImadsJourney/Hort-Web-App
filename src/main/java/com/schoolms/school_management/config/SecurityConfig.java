package com.schoolms.school_management.config;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

/**
 * SecurityConfig
 */
@Configuration
public class SecurityConfig {

  @Value("${app.jwt.secret}")
  private String jwtSecret;

  // Hashing mittels dem von Spring gestellten PasswordEncoder
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  private SecretKey secretKey() {
    return new SecretKeySpec(
        jwtSecret.getBytes(StandardCharsets.UTF_8),
        "HmacSHA256");
  }

  @Bean
  public JwtEncoder jwtEncoder() {
    SecretKey key = secretKey();
    return new NimbusJwtEncoder(new ImmutableSecret<>(key));
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    SecretKey key = secretKey();

    return NimbusJwtDecoder
        .withSecretKey(key)
        .macAlgorithm(MacAlgorithm.HS256)
        .build();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers("/health").permitAll()
            .anyRequest().authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
        .build();
  }

}
