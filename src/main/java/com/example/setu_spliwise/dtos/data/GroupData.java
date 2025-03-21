package com.example.setu_spliwise.dtos.data;

import com.example.setu_spliwise.dtos.info.GroupInfo;
import com.example.setu_spliwise.dtos.spec.GroupSpec;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
public class GroupData {
  @Valid private GroupInfo groupInfo;

  @Valid private GroupSpec groupSpec;
}
