package com.example.setu_spliwise.dtos.spec;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnequalSplitSpec extends SplitSpec {

  @Min(value = 0, message = "Amount must be positive")
  private double amount;
}
