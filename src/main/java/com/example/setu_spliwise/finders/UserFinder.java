package com.example.setu_spliwise.finders;

import com.example.setu_spliwise.models.User;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UserFinder extends BaseFinder<User> {

  public UserFinder() {
    super(com.example.setu_spliwise.models.User.class);
  }

  public User createUser(String email, String name, String password) {
    User user = new User();
    user.setEmail(email);
    user.setName(name);
    user.setPassword(password);
    user.save();
    return user;
  }

  public Optional<User> findByEmail(String email) {
    return query().where().eq("email", email).findOneOrEmpty();
  }
}
