package com.example.setu_spliwise.dtos.info;

import com.example.setu_spliwise.dtos.data.UserData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import java.util.Set;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
public class GroupInfo {

  @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
  @NonNull
  private UUID groupId;

  @Valid @NonNull private Set<UserData> userData;
}
