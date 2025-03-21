package com.example.setu_spliwise.models;

import io.ebean.Model;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserGroup extends Model {

  @Embeddable
  @Getter
  @Setter
  @Data
  public static class UserGroupId implements Serializable {

    private UUID userId;
    private UUID groupId;
  }

  @EmbeddedId private UserGroupId id;

  @ManyToOne
  @MapsId("userId")
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;

  @ManyToOne
  @MapsId("groupId")
  @JoinColumn(name = "group_id", nullable = false, unique = true)
  private Group group;
}
