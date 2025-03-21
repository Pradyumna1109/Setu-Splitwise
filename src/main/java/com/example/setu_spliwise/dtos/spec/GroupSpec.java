package com.example.setu_spliwise.dtos.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
public class GroupSpec {

  @NotBlank(message = "Name is required")
  @NonNull
  @Pattern(
      regexp = "^[A-Za-z][A-Za-z0-9_ ]{2,29}$",
      message =
          "Name must start with a letter and contain only letters, numbers, spaces, or underscores. Length 3-30.")
  private String name;

  @NotBlank(message = "Description is required")
  @NonNull
  private String description;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private Set<UUID> members = new HashSet<>();
}
