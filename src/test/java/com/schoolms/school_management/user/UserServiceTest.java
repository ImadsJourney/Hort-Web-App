package com.schoolms.school_management.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.schoolms.school_management.user.dto.CreateUserRequest;
import com.schoolms.school_management.user.dto.UserResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

/**
 * UserServiceTest
 * UNIT-TEST
 * 
 * Das UserRepository wird gemockt, damit nur die Service-Logik getestet wird.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @Test
  void shouldCreateUser() {
    CreateUserRequest request = new CreateUserRequest("Max", "Mustermann", "some@email.com",
        "somepassword");

    User savedUser = createTestUser();

    when(userRepository.save(any(User.class))).thenReturn(savedUser);

    UserResponse response = userService.createUser(request);

    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.firstName()).isEqualTo("Max");
    assertThat(response.lastName()).isEqualTo("Mustermann");
    assertThat(response.email()).isEqualTo("some@email.com");
    verify(userRepository).save(any(User.class));
  }

  private User createTestUser() {
    User user = new User();
    user.setId(1L);
    user.setFirstName("Max");
    user.setLastName("Mustermann");
    user.setEmail("some@email.com");

    return user;
  }

  @Test
  void shouldReturnAllUsers() {
    User user = createTestUser();

    when(userRepository.findAll()).thenReturn(List.of(user));

    List<UserResponse> response = userService.getAllUsers();

    assertThat(response).hasSize(1);
    assertThat(response.get(0).id()).isEqualTo(1L);
    assertThat(response.get(0).firstName()).isEqualTo("Max");
    assertThat(response.get(0).lastName()).isEqualTo("Mustermann");
    assertThat(response.get(0).email()).isEqualTo("some@email.com");

    verify(userRepository).findAll();

  }

}
