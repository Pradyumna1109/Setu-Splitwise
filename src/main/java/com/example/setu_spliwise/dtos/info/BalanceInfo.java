package com.example.setu_spliwise.dtos.info;

import com.example.setu_spliwise.dtos.enums.BalanceType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
public class BalanceInfo {

  private String groupMember;

  private double due;

  @NonNull private BalanceType type;
}
