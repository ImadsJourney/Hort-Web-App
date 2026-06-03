package com.schoolms.school_management.child;

import com.schoolms.school_management.child.dto.ChildResponse;
import com.schoolms.school_management.child.dto.CreateChildRequest;
import com.schoolms.school_management.hortgroup.HortGroup;
import com.schoolms.school_management.hortgroup.HortGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChildService {

  private final ChildRepository childRepository;
  private final HortGroupRepository hortGroupRepository;

  public ChildResponse createChild(CreateChildRequest request) {
    HortGroup group = hortGroupRepository.findById(request.hortGroupId())
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Hort group not found"));

    Child child = Child.builder()
        .firstName(request.firstName())
        .lastName(request.lastName())
        .notes(request.notes())
        .attendanceStatus(AttendanceStatus.NOT_RECORDED)
        .hortGroup(group)
        .build();

    Child savedChild = childRepository.save(child);

    return toChildResponse(savedChild);
  }

  public List<ChildResponse> getAllChildren() {
    return childRepository.findAll()
        .stream()
        .map(this::toChildResponse)
        .toList();
  }

  public List<ChildResponse> getChildrenByGroupId(Long groupId) {
    return childRepository.findByHortGroupId(groupId)
        .stream()
        .map(this::toChildResponse)
        .toList();
  }

  private ChildResponse toChildResponse(Child child) {
    return new ChildResponse(
        child.getId(),
        child.getFirstName(),
        child.getLastName(),
        child.getAttendanceStatus(),
        child.getNotes(),
        child.getHortGroup().getId(),
        child.getHortGroup().getName());
  }
}
