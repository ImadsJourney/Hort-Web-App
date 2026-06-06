package com.schoolms.school_management.auth;

import com.schoolms.school_management.auth.dto.AuthResponse;
import com.schoolms.school_management.auth.dto.LoginRequest;
import com.schoolms.school_management.auth.dto.RegisterRequest;
import com.schoolms.school_management.user.User;
import com.schoolms.school_management.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JWTService jwtService;

  @InjectMocks
  private AuthService authService;

  @Test
  void shouldRegisterUser() {
    RegisterRequest request = new RegisterRequest("max", "password123");

    User savedUser = createTestUser();

    when(userRepository.existsByUsername("max")).thenReturn(false);
    when(passwordEncoder.encode("password123")).thenReturn("hashed-password");
    when(userRepository.save(any(User.class))).thenReturn(savedUser);
    when(jwtService.generateToken(savedUser)).thenReturn("fake-jwt-token");

    AuthResponse response = authService.register(request);

    assertThat(response.userId()).isEqualTo(1L);
    assertThat(response.username()).isEqualTo("max");
    assertThat(response.message()).isEqualTo("Erfolgreich registriert.");
    assertThat(response.token()).isEqualTo("fake-jwt-token");

    verify(userRepository).existsByUsername("max");
    verify(passwordEncoder).encode("password123");
    verify(userRepository).save(any(User.class));
  }

  @Test
  void shouldThrowConflictWhenUsernameAlreadyExists() {
    RegisterRequest request = new RegisterRequest("max", "password123");

    when(userRepository.existsByUsername("max")).thenReturn(true);

    assertThatThrownBy(() -> authService.register(request))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("Username already exists");

    verify(userRepository).existsByUsername("max");
  }

  @Test
  void shouldLoginUser() {
    LoginRequest request = new LoginRequest("max", "password123");

    User user = createTestUser();

    when(userRepository.findByUsername("max")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("password123", "hashed-password")).thenReturn(true);

    AuthResponse response = authService.login(request);

    assertThat(response.userId()).isEqualTo(1L);
    assertThat(response.username()).isEqualTo("max");
    assertThat(response.message()).isEqualTo("Erfolgreich eingeloggt.");

    verify(userRepository).findByUsername("max");
    verify(passwordEncoder).matches("password123", "hashed-password");
  }

  @Test
  void shouldThrowUnauthorizedWhenPasswordIsWrong() {
    LoginRequest request = new LoginRequest("max", "wrongpassword");

    User user = createTestUser();

    when(userRepository.findByUsername("max")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("wrongpassword", "hashed-password")).thenReturn(false);

    assertThatThrownBy(() -> authService.login(request))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("Invalid username or password");

    verify(userRepository).findByUsername("max");
    verify(passwordEncoder).matches("wrongpassword", "hashed-password");
  }

  private User createTestUser() {
    User user = new User();
    user.setId(1L);
    user.setUsername("max");
    user.setPassword("hashed-password");
    return user;
  }
}
