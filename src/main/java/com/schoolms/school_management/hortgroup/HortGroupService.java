package com.schoolms.school_management.hortgroup;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.schoolms.school_management.hortgroup.dto.CreateHortGroupRequest;
import com.schoolms.school_management.hortgroup.dto.HortGroupResponse;
import com.schoolms.school_management.user.User;
import com.schoolms.school_management.user.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * HortGroupService
 */
@Service
@RequiredArgsConstructor
public class HortGroupService {
  private final HortGroupRepository hortGroupRepository;
  private final UserRepository userRepository;

  public HortGroupResponse createGroup(CreateHortGroupRequest request, Long userId) {
    User owner = userRepository.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "User could not be found."));

    HortGroup group = HortGroup.builder()
        .name(request.name())
        .gradeLevel(request.gradeLevel())
        .supervisorName(request.supervisorName())
        .owner(owner)
        .build();

    HortGroup savedGroup = hortGroupRepository.save(group);

    return toHortGroupResponse(savedGroup);
  }

  public List<HortGroupResponse> getAllGroups(Long userId) {
    return hortGroupRepository.findAllByOwnerId(userId)
        .stream()
        .map(this::toHortGroupResponse)
        .toList();
  }

  private HortGroupResponse toHortGroupResponse(HortGroup group) {
    return new HortGroupResponse(group.getId(), group.getName(), group.getGradeLevel(), group.getSupervisorName());
  }

}
