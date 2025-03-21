package com.example.setu_spliwise.dtos.spec;

import com.example.setu_spliwise.dtos.enums.ExpenseType;
import com.fasterxml.jackson.annotation.*;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = EqualSplitSpec.class, name = "EQUAL"),
  @JsonSubTypes.Type(value = UnequalSplitSpec.class, name = "UNEQUAL"),
  @JsonSubTypes.Type(value = PercentageSplitSpec.class, name = "PERCENTAGE")
})
public abstract class SplitSpec {

  @NonNull private UUID userId;

  @NonNull private ExpenseType type;
}
