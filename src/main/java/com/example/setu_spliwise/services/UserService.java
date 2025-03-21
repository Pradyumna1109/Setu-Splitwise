package com.example.setu_spliwise.services;

import com.example.setu_spliwise.exceptions.ConflictException;
import com.example.setu_spliwise.finders.UserFinder;
import com.example.setu_spliwise.models.User;
import io.ebean.annotation.Transactional;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserFinder userFinder;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(UserFinder userFinder, PasswordEncoder passwordEncoder) {
    this.userFinder = userFinder;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public User create(String email, String name, String password) {
    Optional<User> user = userFinder.findByEmail(email);
    if (user.isPresent()) {
      throw new ConflictException("User with this email already exists");
    }
    password = passwordEncoder.encode(password);
    return userFinder.createUser(email, name, password);
  }

  public List<User> findAll() {
    return userFinder.findAll();
  }

  public User findUserById(UUID id) {
    return userFinder.get(id);
  }

  public List<User> validateUsers(List<UUID> userIds) {
    return userIds.stream().map(userFinder::get).toList();
  }
}
