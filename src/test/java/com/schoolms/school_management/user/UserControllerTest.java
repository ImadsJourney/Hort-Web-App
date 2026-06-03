package com.schoolms.school_management.user;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.schoolms.school_management.user.dto.CreateUserRequest;
import com.schoolms.school_management.user.dto.UserResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import tools.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.mockito.Mockito.verifyNoInteractions;

/**
 * UserControllerTest
 *
 * Wir testen hier ob HTTP POST /users korrekt beim Controller ankommt
 * Wird JSON korrekt gelesen zurückgeben?
 * Richtige HTTP-Message zurückschicken
 *
 */

@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private UserService userService;

  /**
   * Hier wird geürpft ob tatsächlich ein User erstellt wird, ausgehend man hat
   * schon die Konvertierung des JSON-Textes in ein Java Obeject durchgeführt
   *
   *
   */
  @Test
  void shouldCreateUser() throws Exception {
    CreateUserRequest request = new CreateUserRequest("Max", "Mustermann", "some@email.com", "somepassword");

    UserResponse response = new UserResponse(1L, "Max", "Mustermann", "some@email.com");

    when(userService.createUser(any(CreateUserRequest.class))).thenReturn(response);

    mockMvc.perform(post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.firstName").value("Max"))
        .andExpect(jsonPath("$.lastName").value("Mustermann"))
        .andExpect(jsonPath("$.email").value("some@email.com"));
  }

  /**
   *
   * Jetzt prüfen wir noch ob auch ein Fehler zurückgegeben wird,
   * wenn die Validierung fehlschlägt
   *
   *
   */

  @Test
  void shouldReturnBadRequest() throws Exception {
    CreateUserRequest request = new CreateUserRequest("Max", "Mustermann ", "notvalid-email", "whatever");

    mockMvc.perform(post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());

    verifyNoInteractions(userService);
  }

  @Test
  void shouldReturnAllUsers() throws Exception {
    UserResponse userResponse = new UserResponse(
        1L,
        "Max",
        "Mustermann",
        "some@email.com");

    when(userService.getAllUsers()).thenReturn(List.of(userResponse));

    mockMvc.perform(get("/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].firstName").value("Max"))
        .andExpect(jsonPath("$[0].lastName").value("Mustermann"))
        .andExpect(jsonPath("$[0].email").value("some@email.com"));
  }
}
