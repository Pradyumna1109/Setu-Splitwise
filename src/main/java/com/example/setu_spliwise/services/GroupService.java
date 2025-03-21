package com.example.setu_spliwise.services;

import com.example.setu_spliwise.finders.GroupFinder;
import com.example.setu_spliwise.finders.UserFinder;
import com.example.setu_spliwise.finders.UserGroupFinder;
import com.example.setu_spliwise.models.Group;
import com.example.setu_spliwise.models.User;
import io.ebean.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {
  private final GroupFinder groupFinder;
  private final UserFinder userFinder;
  private final UserGroupFinder userGroupFinder;

  @Autowired
  public GroupService(
      GroupFinder groupFinder, UserFinder userFinder, UserGroupFinder userGroupFinder) {
    this.groupFinder = groupFinder;
    this.userFinder = userFinder;
    this.userGroupFinder = userGroupFinder;
  }

  @Transactional
  public Group create(String name, String description, Set<UUID> memberIds) {
    Group group = groupFinder.createGroup(name, description);
    memberIds.add(UUID.fromString(MDC.get("user-id")));
    group.setUsers(getUsers(memberIds));
    group.save();
    group.refresh();
    return group;
  }

  private Set<User> getUsers(Set<UUID> memberIds) {
    return memberIds.stream().map(userFinder::get).collect(Collectors.toSet());
  }

  public Group update(UUID id, Set<UUID> memberIds) {
    Group group = groupFinder.get(id);

    group.getUsers().addAll(getUsers(memberIds));
    group.update();
    return group;
  }

  public List<Group> findAllByUserId(UUID userId) {
    return userGroupFinder.findAllByUser(userId);
  }

  public boolean isUserInGroup(UUID groupId, UUID userId) {
    return userGroupFinder.findUserInGroup(groupId, userId);
  }

  public Group findGroupById(UUID id) {
    return groupFinder.get(id);
  }
}
