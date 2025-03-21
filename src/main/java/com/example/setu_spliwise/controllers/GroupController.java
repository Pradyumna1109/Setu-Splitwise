package com.example.setu_spliwise.controllers;

import com.example.setu_spliwise.common.Authenticate;
import com.example.setu_spliwise.common.GroupAuthorize;
import com.example.setu_spliwise.dtos.CreateGroupDto;
import com.example.setu_spliwise.dtos.GroupResponseDto;
import com.example.setu_spliwise.dtos.UpdateGroupDto;
import com.example.setu_spliwise.dtos.spec.GroupSpec;
import com.example.setu_spliwise.models.Group;
import com.example.setu_spliwise.services.GroupService;
import com.example.setu_spliwise.util.ApiUtil;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/groups")
public class GroupController {
  private final GroupService groupService;

  @Autowired
  public GroupController(GroupService groupService) {
    this.groupService = groupService;
  }

  // Create a new group
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Authenticate
  public ResponseEntity<GroupResponseDto> createGroup(
      @RequestBody @Valid CreateGroupDto createGroupDto) {
    GroupSpec groupSpec = createGroupDto.getGroupSpec();
    Group group =
        groupService.create(
            groupSpec.getName(), groupSpec.getDescription(), groupSpec.getMembers());
    GroupResponseDto groupResponseDto = ApiUtil.getGroupResponseDto(group);
    return new ResponseEntity<>(groupResponseDto, HttpStatus.CREATED);
  }

  @PutMapping("/{groupId}")
  @ResponseStatus(HttpStatus.OK)
  @Authenticate
  @GroupAuthorize
  public ResponseEntity<GroupResponseDto> updateGroup(
      @Valid @PathVariable UUID groupId, @RequestBody @Valid UpdateGroupDto updateGroupDto) {
    Group group = groupService.update(groupId, updateGroupDto.getGroupSpec().getMembers());
    GroupResponseDto groupResponseDto = ApiUtil.getGroupResponseDto(group);
    return new ResponseEntity<>(groupResponseDto, HttpStatus.CREATED);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Authenticate
  public ResponseEntity<List<GroupResponseDto>> findAllGroupsByUserId() {
    UUID userId = UUID.fromString(MDC.get("user-id"));
    List<Group> groups = groupService.findAllByUserId(userId);
    List<GroupResponseDto> groupsDto = groups.stream().map(ApiUtil::getGroupResponseDto).toList();
    return new ResponseEntity<>(groupsDto, HttpStatus.OK);
  }

  // Get group by ID
  @GetMapping("/{groupId}")
  @ResponseStatus(HttpStatus.OK)
  @Authenticate
  @GroupAuthorize
  public ResponseEntity<GroupResponseDto> findGroupById(@Valid @PathVariable UUID groupId) {
    Group group = groupService.findGroupById(groupId);
    GroupResponseDto groupResponseDto = ApiUtil.getGroupResponseDto(group);
    return new ResponseEntity<>(groupResponseDto, HttpStatus.OK);
  }
}
