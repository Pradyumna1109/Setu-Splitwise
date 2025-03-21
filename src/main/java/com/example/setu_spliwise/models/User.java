package com.example.setu_spliwise.models;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "USERS")
public class User extends BaseModel {

  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "password")
  private String password;
}
