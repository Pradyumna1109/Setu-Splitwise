package com.example.setu_spliwise.dtos;

import com.example.setu_spliwise.dtos.spec.AuthSpec;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
public class AuthDto {
  @Valid private AuthSpec authSpec;
}
