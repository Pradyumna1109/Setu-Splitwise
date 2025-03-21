package com.example.setu_spliwise.dtos.spec;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PercentageSplitSpec extends SplitSpec {

  @Min(value = 0, message = "Percentage must be positive")
  @Max(value = 100, message = "Percentage cannot be more than 100")
  private double percentage;
}
