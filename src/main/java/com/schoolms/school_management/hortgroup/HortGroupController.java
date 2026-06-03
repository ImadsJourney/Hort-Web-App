package com.schoolms.school_management.hortgroup;

import java.util.List;

import org.springframework.http.HttpStatus;
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

/**
 * HortGroupController
 */
@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class HortGroupController {
  private final HortGroupService hortGroupService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public HortGroupResponse createGroup(@Valid @RequestBody CreateHortGroupRequest request) {
    return hortGroupService.createGroup(request);
  }

  @GetMapping
  public List<HortGroupResponse> getAllGroups() {
    return hortGroupService.getAllGroups();
  }

}
