package com.schoolms.school_management.child;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.schoolms.school_management.child.dto.ChildResponse;
import com.schoolms.school_management.child.dto.CreateChildRequest;
import com.schoolms.school_management.child.dto.UpdateAttendanceRequest;
import com.schoolms.school_management.child.dto.UpdateNotesRequest;
import com.schoolms.school_management.hortgroup.HortGroup;
import com.schoolms.school_management.hortgroup.HortGroupRepository;

@ExtendWith(MockitoExtension.class)
class ChildServiceTest {

  private static final Long userId = 10L;
  private static final Long groupId = 1L;
  private static final Long childId = 1L;

  @Mock
  private ChildRepository childRepository;

  @Mock
  private HortGroupRepository hortGroupRepository;

  @InjectMocks
  private ChildService childService;

  private HortGroup createTestGroup() {
    HortGroup group = new HortGroup();
    group.setId(groupId);
    group.setName("Bärengruppe");
    group.setGradeLevel("1. Klasse");
    group.setSupervisorName("Frau Müller");

    return group;
  }

  private Child createTestChild(HortGroup group) {
    Child child = new Child();
    child.setId(childId);
    child.setFirstName("Emma");
    child.setLastName("Schmidt");
    child.setNotes("Wird um 15 Uhr abgeholt");
    child.setAttendanceStatus(AttendanceStatus.NOT_RECORDED);
    child.setHortGroup(group);

    return child;
  }

  @Test
  void shouldCreateChild() {
    CreateChildRequest request = new CreateChildRequest(
        "Emma",
        "Schmidt",
        "Wird um 15 Uhr abgeholt",
        groupId);

    HortGroup group = createTestGroup();
    Child savedChild = createTestChild(group);

    when(hortGroupRepository.findByIdAndOwnerId(groupId, userId))
        .thenReturn(Optional.of(group));

    when(childRepository.save(any(Child.class)))
        .thenReturn(savedChild);

    ChildResponse response = childService.createChild(request, userId);

    assertThat(response.id()).isEqualTo(childId);
    assertThat(response.firstName()).isEqualTo("Emma");
    assertThat(response.lastName()).isEqualTo("Schmidt");
    assertThat(response.notes())
        .isEqualTo("Wird um 15 Uhr abgeholt");
    assertThat(response.attendanceStatus())
        .isEqualTo(AttendanceStatus.NOT_RECORDED);
    assertThat(response.hortGroupId()).isEqualTo(groupId);
    assertThat(response.hortGroupName()).isEqualTo("Bärengruppe");

    verify(hortGroupRepository)
        .findByIdAndOwnerId(groupId, userId);

    verify(childRepository).save(any(Child.class));
  }

  @Test
  void shouldUpdateAttendance() {
    HortGroup group = createTestGroup();
    Child child = createTestChild(group);

    UpdateAttendanceRequest request = new UpdateAttendanceRequest(AttendanceStatus.PRESENT);

    when(childRepository.findByIdAndHortGroupOwnerId(
        childId,
        userId)).thenReturn(Optional.of(child));

    when(childRepository.save(any(Child.class)))
        .thenReturn(child);

    ChildResponse response = childService.updateAttendance(
        childId,
        request,
        userId);

    assertThat(response.id()).isEqualTo(childId);
    assertThat(response.attendanceStatus())
        .isEqualTo(AttendanceStatus.PRESENT);

    verify(childRepository)
        .findByIdAndHortGroupOwnerId(childId, userId);

    verify(childRepository).save(any(Child.class));
  }

  @Test
  void shouldUpdateNotes() {
    HortGroup group = createTestGroup();
    Child child = createTestChild(group);

    UpdateNotesRequest request = new UpdateNotesRequest("Neue Notiz");

    when(childRepository.findByIdAndHortGroupOwnerId(
        childId,
        userId)).thenReturn(Optional.of(child));

    when(childRepository.save(any(Child.class)))
        .thenReturn(child);

    ChildResponse response = childService.updateNotes(
        childId,
        request,
        userId);

    assertThat(response.id()).isEqualTo(childId);
    assertThat(response.notes()).isEqualTo("Neue Notiz");

    verify(childRepository)
        .findByIdAndHortGroupOwnerId(childId, userId);

    verify(childRepository).save(any(Child.class));
  }
}
