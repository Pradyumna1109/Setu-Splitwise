package com.example.setu_spliwise.dtos;

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
public class CreateGroupDto {
  @Valid private GroupSpec groupSpec;
}
