package com.example.setu_spliwise.dtos.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
public class AuthInfo {

  @JsonProperty(value = "JWT", access = JsonProperty.Access.READ_ONLY)
  @NonNull
  private String token;
}
