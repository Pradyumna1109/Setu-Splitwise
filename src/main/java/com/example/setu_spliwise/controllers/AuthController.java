package com.example.setu_spliwise.controllers;

import com.example.setu_spliwise.dtos.AuthDto;
import com.example.setu_spliwise.dtos.AuthResponseDto;
import com.example.setu_spliwise.exceptions.UnAuthorisedException;
import com.example.setu_spliwise.finders.UserFinder;
import com.example.setu_spliwise.models.User;
import com.example.setu_spliwise.util.ApiUtil;
import com.example.setu_spliwise.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private JwtUtil jwtUtil;
  private UserFinder userFinder;

  @Autowired
  public AuthController(PasswordEncoder passwordEncoder, JwtUtil jwtUtil, UserFinder userFinder) {
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
    this.userFinder = userFinder;
  }

  private PasswordEncoder passwordEncoder;

  @PostMapping("/generate")
  public ResponseEntity<AuthResponseDto> generateToken(@RequestBody @Valid AuthDto authDto) {
    User user = userFinder.findByEmail(authDto.getAuthSpec().getEmail()).orElse(null);

    if (user == null
        || !passwordEncoder.matches(authDto.getAuthSpec().getPassword(), user.getPassword())) {
      throw new UnAuthorisedException("Invalid Credentials");
    }

    String token = jwtUtil.generateToken(user.getId());
    return new ResponseEntity<>(
        ApiUtil.getAuthResponseDto(authDto.getAuthSpec(), token), HttpStatus.CREATED);
  }
}
