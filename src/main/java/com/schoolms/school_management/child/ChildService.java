package com.schoolms.school_management.child;

import com.schoolms.school_management.child.dto.ChildResponse;
import com.schoolms.school_management.child.dto.CreateChildRequest;
import com.schoolms.school_management.child.dto.UpdateAttendanceRequest;
import com.schoolms.school_management.child.dto.UpdateNotesRequest;
import com.schoolms.school_management.hortgroup.HortGroup;
import com.schoolms.school_management.hortgroup.HortGroupRepository;
import com.schoolms.school_management.user.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChildService {

  private final ChildRepository childRepository;
  private final HortGroupRepository hortGroupRepository;

  public ChildResponse createChild(CreateChildRequest request, Long userId) {

    HortGroup group = hortGroupRepository.findByIdAndOwnerId(request.hortGroupId(), userId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Group not found or access denied."));

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

  public List<ChildResponse> getAllChildren(Long userId) {
    return childRepository
        .findAllByHortGroupOwnerIdOrderByLastNameAscFirstNameAsc(userId)
        .stream()
        .map(this::toChildResponse)
        .toList();
  }

  public List<ChildResponse> getChildrenByGroupId(Long groupId, Long userId) {
    return childRepository.findByHortGroupIdAndHortGroupOwnerIdOrderByLastNameAscFirstNameAsc(groupId, userId)
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

  public ChildResponse updateAttendance(Long childId, UpdateAttendanceRequest request, Long userId) {
    Child child = childRepository.findByIdAndHortGroupOwnerId(childId, userId).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Child not found."));

    child.setAttendanceStatus(request.attendanceStatus());

    Child savedChild = childRepository.save(child);

    return toChildResponse(savedChild);
  }

  public ChildResponse updateNotes(Long childId, UpdateNotesRequest request, Long userId) {
    Child child = childRepository.findByIdAndHortGroupOwnerId(childId, userId).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Child not found."));

    child.setNotes(request.notes());

    Child savedChild = childRepository.save(child);

    return toChildResponse(savedChild);
  }
}
