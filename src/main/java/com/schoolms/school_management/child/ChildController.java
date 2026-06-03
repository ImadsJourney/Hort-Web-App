package com.schoolms.school_management.child;

import com.schoolms.school_management.child.dto.ChildResponse;
import com.schoolms.school_management.child.dto.CreateChildRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChildController {

  private final ChildService childService;

  @PostMapping("/children")
  @ResponseStatus(HttpStatus.CREATED)
  public ChildResponse createChild(@Valid @RequestBody CreateChildRequest request) {
    return childService.createChild(request);
  }

  @GetMapping("/children")
  public List<ChildResponse> getAllChildren() {
    return childService.getAllChildren();
  }

  @GetMapping("/groups/{groupId}/children")
  public List<ChildResponse> getChildrenByGroup(@PathVariable Long groupId) {
    return childService.getChildrenByGroupId(groupId);
  }
}
