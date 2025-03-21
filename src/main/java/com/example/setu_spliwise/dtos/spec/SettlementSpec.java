package com.example.setu_spliwise.dtos.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
public class SettlementSpec {

  private UUID expenseId;
}
