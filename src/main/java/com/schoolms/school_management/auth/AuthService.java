package com.schoolms.school_management.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.schoolms.school_management.auth.dto.AuthResponse;
import com.schoolms.school_management.auth.dto.LoginRequest;
import com.schoolms.school_management.auth.dto.RegisterRequest;
import com.schoolms.school_management.user.User;
import com.schoolms.school_management.user.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * AuthService
 */
@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JWTService jwtService;

  public AuthResponse register(RegisterRequest request) {
    if (userRepository.existsByUsername(request.username()) == true) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
    }

    User user = User.builder()
        .username(request.username())
        .password(passwordEncoder.encode(request.password()))
        .build();

    User savedUser = userRepository.save(user);

    String token = jwtService.generateToken(savedUser);

    return new AuthResponse(
        savedUser.getId(),
        savedUser.getUsername(),
        token,
        "Erfolgreich registriert.");

  }

  public AuthResponse login(LoginRequest request) {
    User user = userRepository.findByUsername(request.username())
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "Invalid username or password"));

    boolean passwordMatches = passwordEncoder.matches(request.password(), user.getPassword());

    if (!passwordMatches) {
      throw new ResponseStatusException(
          HttpStatus.UNAUTHORIZED,
          "Invalid username or password");
    }

    String token = jwtService.generateToken(user);

    return new AuthResponse(
        user.getId(),
        user.getUsername(),
        token,
        "Erfolgreich eingeloggt.");
  }

}
