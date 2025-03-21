package com.example.setu_spliwise.finders;

import com.example.setu_spliwise.models.Group;
import com.example.setu_spliwise.models.UserGroup;
import io.ebean.Finder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class UserGroupFinder extends Finder<UUID, UserGroup> {

  public UserGroupFinder() {
    super(UserGroup.class);
  }

  public List<Group> findAllByUser(UUID userId) {
    return query().select("group").where().eq("user.id", userId).findList().stream()
        .map(userGroup -> userGroup.getGroup())
        .toList();
  }

  public boolean findUserInGroup(UUID groupId, UUID userId) {
    Optional<UserGroup> userGroup =
        query().where().eq("user_id", userId).eq("group_id", groupId).findOneOrEmpty();
    return userGroup.isPresent();
  }

  public Optional<UserGroup> getUserInGroup(UUID groupId, UUID userId) {
    Optional<UserGroup> userGroup =
        query().where().eq("user_id", userId).eq("group_id", groupId).findOneOrEmpty();
    return userGroup;
  }
}
