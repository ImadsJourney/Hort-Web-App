package com.schoolms.school_management.hortgroup;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.schoolms.school_management.hortgroup.dto.CreateHortGroupRequest;
import com.schoolms.school_management.hortgroup.dto.HortGroupResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * HortGroupController
 */
@Slf4j
@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class HortGroupController {
  private final HortGroupService hortGroupService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public HortGroupResponse createGroup(@Valid @RequestBody CreateHortGroupRequest request,
      @AuthenticationPrincipal Jwt jwt) {

    Long userId = ((Number) jwt.getClaim("userId")).longValue();

    return hortGroupService.createGroup(request, userId);
  }

  @GetMapping
  public List<HortGroupResponse> getAllGroups(
      @AuthenticationPrincipal Jwt jwt) {
    Long userId = ((Number) jwt.getClaim("userId")).longValue();

    log.info("Loading groups for userId {}", userId);

    return hortGroupService.getAllGroups(userId);
  }

}
