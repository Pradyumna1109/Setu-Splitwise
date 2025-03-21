package com.example.setu_spliwise.dtos.data;

import com.example.setu_spliwise.dtos.info.BalanceInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
public class BalanceData {
  @Valid private List<BalanceInfo> balanceInfo;
}
