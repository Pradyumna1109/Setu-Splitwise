package com.example.setu_spliwise.dtos;

import com.example.setu_spliwise.dtos.data.BalanceData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
public class BalanceResponseDto {
  @Valid private BalanceData balanceData;
}
