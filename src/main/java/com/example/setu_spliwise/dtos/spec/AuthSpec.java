package com.example.setu_spliwise.dtos.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
public class AuthSpec {
  @Email(message = "Invalid email address")
  @NotBlank(message = "Email is required")
  @NonNull
  private String email;

  @NotBlank(message = "Name is required")
  @NonNull
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Pattern(
      regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$",
      message =
          "Password must be 8-20 characters, contain at least one letter, one number, and one special character.")
  private String password;
}
