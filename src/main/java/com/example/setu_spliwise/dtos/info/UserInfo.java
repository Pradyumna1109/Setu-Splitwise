package com.example.setu_spliwise.dtos.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
public class UserInfo {

  @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
  @NonNull
  private UUID userId;
}
