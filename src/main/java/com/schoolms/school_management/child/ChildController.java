package com.schoolms.school_management.child;

import com.schoolms.school_management.child.dto.ChildResponse;
import com.schoolms.school_management.child.dto.CreateChildRequest;
import com.schoolms.school_management.child.dto.UpdateAttendanceRequest;
import com.schoolms.school_management.child.dto.UpdateNotesRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChildController {

  private final ChildService childService;

  @PostMapping("/children")
  @ResponseStatus(HttpStatus.CREATED)
  public ChildResponse createChild(
      @Valid @RequestBody CreateChildRequest request,
      @AuthenticationPrincipal Jwt jwt) {
    Long userId = getUserId(jwt);

    return childService.createChild(request, userId);
  }

  @GetMapping("/children")
  public List<ChildResponse> getAllChildren(
      @AuthenticationPrincipal Jwt jwt) {
    Long userId = getUserId(jwt);

    return childService.getAllChildren(userId);
  }

  @GetMapping("/groups/{groupId}/children")
  public List<ChildResponse> getChildrenByGroup(
      @PathVariable Long groupId,
      @AuthenticationPrincipal Jwt jwt) {
    Long userId = getUserId(jwt);

    return childService.getChildrenByGroupId(groupId, userId);
  }

  @PatchMapping("/children/{id}/attendance")
  public ChildResponse updateAttendance(
      @PathVariable Long id,
      @Valid @RequestBody UpdateAttendanceRequest request,
      @AuthenticationPrincipal Jwt jwt) {
    Long userId = getUserId(jwt);

    return childService.updateAttendance(id, request, userId);
  }

  @PatchMapping("/children/{id}/notes")
  public ChildResponse updateNotes(
      @PathVariable Long id,
      @Valid @RequestBody UpdateNotesRequest request,
      @AuthenticationPrincipal Jwt jwt) {
    Long userId = getUserId(jwt);

    return childService.updateNotes(id, request, userId);
  }

  private Long getUserId(Jwt jwt) {
    return ((Number) jwt.getClaim("userId")).longValue();
  }
}
