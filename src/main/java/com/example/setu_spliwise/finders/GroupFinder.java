package com.example.setu_spliwise.finders;

import com.example.setu_spliwise.models.Group;
import org.springframework.stereotype.Repository;

@Repository
public class GroupFinder extends BaseFinder<Group> {

  public GroupFinder() {
    super(Group.class);
  }

  public Group createGroup(String name, String description) {
    Group group = new Group();
    group.setName(name);
    group.setDescription(description);
    group.save();
    return group;
  }
}
