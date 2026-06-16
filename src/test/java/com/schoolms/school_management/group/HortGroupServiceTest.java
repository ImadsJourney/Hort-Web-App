package com.schoolms.school_management.group;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.schoolms.school_management.hortgroup.HortGroup;
import com.schoolms.school_management.hortgroup.HortGroupRepository;
import com.schoolms.school_management.hortgroup.HortGroupService;
import com.schoolms.school_management.hortgroup.dto.CreateHortGroupRequest;
import com.schoolms.school_management.hortgroup.dto.HortGroupResponse;
import com.schoolms.school_management.user.User;
import com.schoolms.school_management.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class HortGroupServiceTest {

  private static final Long userId = 10L;

  @Mock
  private HortGroupRepository hortGroupRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private User owner;

  @InjectMocks
  private HortGroupService hortGroupService;

  private HortGroup createTestGroup() {
    HortGroup group = new HortGroup();
    group.setId(1L);
    group.setName("Gruppe ALöcher");
    group.setGradeLevel("3. Klasse");
    group.setSupervisorName("Herr Müller");
    group.setOwner(owner);

    return group;
  }

  @Test
  void shouldCreateGroup() {
    CreateHortGroupRequest request = new CreateHortGroupRequest(
        "Gruppe ALöcher",
        "3. Klasse",
        "Herr Müller");

    HortGroup savedGroup = createTestGroup();

    when(userRepository.findById(userId))
        .thenReturn(Optional.of(owner));

    when(hortGroupRepository.save(any(HortGroup.class)))
        .thenReturn(savedGroup);

    HortGroupResponse response = hortGroupService.createGroup(request, userId);

    assertThat(response.id()).isEqualTo(1L);
    assertThat(response.name()).isEqualTo("Gruppe ALöcher");
    assertThat(response.gradeLevel()).isEqualTo("3. Klasse");
    assertThat(response.supervisorName()).isEqualTo("Herr Müller");

    verify(userRepository).findById(userId);
    verify(hortGroupRepository).save(any(HortGroup.class));
  }

  @Test
  void shouldReturnAllGroups() {
    HortGroup hortGroup = createTestGroup();

    when(hortGroupRepository.findAllByOwnerId(userId))
        .thenReturn(List.of(hortGroup));

    List<HortGroupResponse> responses = hortGroupService.getAllGroups(userId);

    assertThat(responses).hasSize(1);
    assertThat(responses.get(0).id()).isEqualTo(1L);
    assertThat(responses.get(0).name())
        .isEqualTo("Gruppe ALöcher");
    assertThat(responses.get(0).gradeLevel())
        .isEqualTo("3. Klasse");
    assertThat(responses.get(0).supervisorName())
        .isEqualTo("Herr Müller");

    verify(hortGroupRepository).findAllByOwnerId(userId);
  }
}
