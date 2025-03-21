package com.example.setu_spliwise.dtos.data;

import com.example.setu_spliwise.dtos.info.SplitInfo;
import com.example.setu_spliwise.dtos.spec.SplitSpec;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
public class SplitData {
  @Valid private SplitInfo splitInfo;

  @Valid private SplitSpec splitSpec;
}
