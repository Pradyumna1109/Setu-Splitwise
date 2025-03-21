package com.example.setu_spliwise.dtos.spec;

import com.example.setu_spliwise.dtos.enums.ExpenseType;
import com.example.setu_spliwise.dtos.info.SplitInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
// @JsonDeserialize(using = ExpenseSpecDeserialiser.class)
public class ExpenseSpec {

  @NotBlank(message = "Description is required")
  @NonNull
  private String description;

  @Min(0)
  private double amount;

  @NonNull private ExpenseType type;

  @Valid
  @NonNull
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private Set<SplitSpec> splits = new HashSet<>();

  @JsonProperty(value = "splitInfo", access = JsonProperty.Access.READ_ONLY)
  @NonNull
  private Set<SplitInfo> splitInfo;
}
