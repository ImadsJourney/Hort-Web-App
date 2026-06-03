package com.schoolms.school_management.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.schoolms.school_management.user.dto.CreateUserRequest;
import com.schoolms.school_management.user.dto.UserResponse;

import lombok.RequiredArgsConstructor;

/**
 * UserService
 *
 * Dieser Teil verwaltet die eigentliche Application Logic
 * Die Logik der Endpoints findet tatsächlich hier statt
 */
@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public UserResponse createUser(CreateUserRequest request) {
    User user = User.builder()
        .firstName(request.firstName())
        .lastName(request.lastName())
        .email(request.email())
        .password(request.password())
        .build();

    User savedUser = userRepository.save(user);
    return toUserResponse(savedUser);
  }

  /*
   * return new UserResponse(
   * savedUser.getId(),
   * savedUser.getFirstName(),
   * savedUser.getLastName(),
   * savedUser.getEmail(),
   * savedUser.getRole());
   * // Aus irgendeinem Grund auch hier zeigt er mei LSP in Neovim nicht an das
   * ich
   * // die Getter benutzen kann -> funktionieren tut es trotzdem
   */

  private UserResponse toUserResponse(User user) {
    return new UserResponse(
        user.getId(),
        user.getFirstName(),
        user.getLastName(),
        user.getEmail());
  }

  public List<UserResponse> getAllUsers() {
    List<User> users = userRepository.findAll();
    List<UserResponse> responses = new ArrayList<>();

    for (User user : users) {
      UserResponse response = toUserResponse(user);
      responses.add(response);
    }

    return responses;
  }
}
