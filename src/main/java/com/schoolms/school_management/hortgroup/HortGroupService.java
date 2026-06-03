package com.schoolms.school_management.hortgroup;

import java.util.List;

import org.springframework.stereotype.Service;

import com.schoolms.school_management.hortgroup.dto.CreateHortGroupRequest;
import com.schoolms.school_management.hortgroup.dto.HortGroupResponse;

import lombok.RequiredArgsConstructor;

/**
 * HortGroupService
 */
@Service
@RequiredArgsConstructor
public class HortGroupService {
  private final HortGroupRepository hortGroupRepository;

  public HortGroupResponse createGroup(CreateHortGroupRequest request) {
    HortGroup group = HortGroup.builder()
        .name(request.name())
        .gradeLevel(request.name())
        .supervisorName(request.supervisorName())
        .build();

    HortGroup savedGroup = hortGroupRepository.save(group);

    return toHortGroupResponse(savedGroup);
  }

  public List<HortGroupResponse> getAllGroups() {
    return hortGroupRepository.findAll()
        .stream()
        .map(this::toHortGroupResponse)
        .toList();
  }

  private HortGroupResponse toHortGroupResponse(HortGroup group) {
    return new HortGroupResponse(group.getId(), group.getName(), group.getGradeLevel(), group.getSupervisorName());
  }

}
