
package com.schoolms.school_management.group;

import com.schoolms.school_management.hortgroup.HortGroupController;
import com.schoolms.school_management.hortgroup.HortGroupService;
import com.schoolms.school_management.hortgroup.dto.CreateHortGroupRequest;
import com.schoolms.school_management.hortgroup.dto.HortGroupResponse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.context.annotation.Import;

import com.schoolms.school_management.config.SecurityConfig;

@WebMvcTest(HortGroupController.class)
@Import(SecurityConfig.class)
class HortGroupControllerTest {

  private static final Long userId = 1L;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private HortGroupService hortGroupService;

  @MockitoBean
  private JwtDecoder jwtDecoder;

  @Test
  void shouldCreateGroup() throws Exception {
    CreateHortGroupRequest request = new CreateHortGroupRequest(
        "ALöcher",
        "3. Klasse",
        "Herr Müller");

    HortGroupResponse response = createGroupResponse();

    when(hortGroupService.createGroup(
        any(CreateHortGroupRequest.class),
        eq(userId))).thenReturn(response);

    mockMvc.perform(post("/groups")
        .with(jwt().jwt(token -> token.claim("userId", userId)))
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("ALöcher"))
        .andExpect(jsonPath("$.gradeLevel").value("3. Klasse"))
        .andExpect(jsonPath("$.supervisorName")
            .value("Herr Müller"));
  }

  @Test
  void shouldReturnAllGroups() throws Exception {
    HortGroupResponse response = createGroupResponse();

    when(hortGroupService.getAllGroups(userId))
        .thenReturn(List.of(response));

    mockMvc.perform(get("/groups")
        .with(jwt().jwt(token -> token.claim("userId", userId))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].name").value("ALöcher"))
        .andExpect(jsonPath("$[0].gradeLevel")
            .value("3. Klasse"))
        .andExpect(jsonPath("$[0].supervisorName")
            .value("Herr Müller"));
  }

  @Test
  void shouldReturnBadRequestWhenGroupNameIsBlank()
      throws Exception {

    CreateHortGroupRequest request = new CreateHortGroupRequest(
        "",
        "3. Klasse",
        "Herr Müller");

    mockMvc.perform(post("/groups")
        .with(jwt().jwt(token -> token.claim("userId", userId)))
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());

    verifyNoInteractions(hortGroupService);
  }

  private HortGroupResponse createGroupResponse() {
    return new HortGroupResponse(
        1L,
        "ALöcher",
        "3. Klasse",
        "Herr Müller");
  }
}
