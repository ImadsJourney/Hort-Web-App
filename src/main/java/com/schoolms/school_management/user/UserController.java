package com.schoolms.school_management.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.schoolms.school_management.user.dto.CreateUserRequest;
import com.schoolms.school_management.user.dto.UserResponse;

import jakarta.validation.Valid;

/**
 * UserController
 */

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
    return userService.createUser(request);
  }

  @GetMapping
  public List<UserResponse> getAllUsers() {
    return userService.getAllUsers();
  }

}
