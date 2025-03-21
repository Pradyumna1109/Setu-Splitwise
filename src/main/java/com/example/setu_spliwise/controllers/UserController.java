package com.example.setu_spliwise.controllers;

import com.example.setu_spliwise.common.Authenticate;
import com.example.setu_spliwise.dtos.CreateUserDto;
import com.example.setu_spliwise.dtos.UserResponseDto;
import com.example.setu_spliwise.dtos.spec.UserSpec;
import com.example.setu_spliwise.models.User;
import com.example.setu_spliwise.services.UserService;
import com.example.setu_spliwise.util.ApiUtil;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  // Create a new user
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<UserResponseDto> createUser(
      @RequestBody @Valid CreateUserDto createUserDto) {
    UserSpec userSpec = createUserDto.getUserSpec();
    User user = userService.create(userSpec.getEmail(), userSpec.getName(), userSpec.getPassword());
    UserResponseDto userDto = ApiUtil.getUserResponseDto(user);
    return new ResponseEntity<>(userDto, HttpStatus.CREATED);
  }

  // Get user by auth token
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Authenticate
  public ResponseEntity<UserResponseDto> findUserById() {
    UUID userId = UUID.fromString(MDC.get("user-id"));
    User user = userService.findUserById(userId);
    UserResponseDto userDto = ApiUtil.getUserResponseDto(user);
    return new ResponseEntity<>(userDto, HttpStatus.OK);
  }
}
