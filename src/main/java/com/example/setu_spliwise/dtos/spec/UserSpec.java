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
public class UserSpec {
  @NotBlank(message = "Name is required")
  @NonNull
  @Pattern(
      regexp = "^[A-Za-z][A-Za-z0-9_ ]{2,29}$",
      message =
          "Name must start with a letter and contain only letters, numbers, spaces, or underscores. Length 3-30.")
  private String name;

  @Email(message = "Invalid email address")
  @NotBlank(message = "Email is required")
  @NonNull
  private String email;

  @NotBlank(message = "Password is required")
  @NonNull
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Pattern(
      regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$",
      message =
          "Password must be 8-20 characters, contain at least one letter, one number, and one special character.")
  private String password;
}
