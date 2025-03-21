package com.example.setu_spliwise.util.expenseUtil;

import com.example.setu_spliwise.dtos.spec.EqualSplitSpec;
import com.example.setu_spliwise.dtos.spec.ExpenseSpec;
import com.example.setu_spliwise.dtos.spec.SplitSpec;
import com.example.setu_spliwise.exceptions.BadRequestException;
import com.example.setu_spliwise.finders.SplitFinder;
import com.example.setu_spliwise.models.Expense;
import com.example.setu_spliwise.models.Split;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EqualSplitStrategy implements SplitStrategy {

  private final SplitFinder splitFinder;

  @Autowired
  public EqualSplitStrategy(SplitFinder splitFinder) {
    this.splitFinder = splitFinder;
  }

  private static final BigDecimal TOLERANCE = BigDecimal.valueOf(0.01);

  @Override
  public void validate(double amount, Set<SplitSpec> splits) {
    if (splits == null || splits.isEmpty()) {
      throw new BadRequestException("Splits cannot be empty");
    }

    double expectedEqualAmount = amount / (splits.size());
    BigDecimal bd = BigDecimal.valueOf(expectedEqualAmount).setScale(2, RoundingMode.HALF_UP);
    double equalSplitAmount = bd.doubleValue();

    BigDecimal expectedAmount = BigDecimal.valueOf(equalSplitAmount);

    for (SplitSpec split : splits) {
      EqualSplitSpec splitSpec = (EqualSplitSpec) split;
      BigDecimal splitAmount = BigDecimal.valueOf(splitSpec.getAmount());

      BigDecimal difference = splitAmount.subtract(expectedAmount).abs();

      if (difference.compareTo(TOLERANCE) > 0) {
        throw new BadRequestException(
            String.format(
                "For EQUAL split type, all amounts must be equal to %.2f", equalSplitAmount));
      }
    }
  }
  ;

  @Override
  public Set<Split> createSplit(ExpenseSpec expenseSpec, Expense expense) {
    double percentage = (double) 100 / expenseSpec.getSplits().size();
    BigDecimal bd = BigDecimal.valueOf(percentage).setScale(2, RoundingMode.HALF_UP);
    double result = bd.doubleValue();
    return expenseSpec.getSplits().stream()
        .map(
            split -> {
              EqualSplitSpec splitSpec = (EqualSplitSpec) split;
              return splitFinder.createSplit(
                  splitSpec.getAmount(), result, splitSpec.getUserId(), expense);
            })
        .collect(Collectors.toSet());
  }

  @Override
  public SplitSpec getSplitSpec(Split split) {
    EqualSplitSpec spec = new EqualSplitSpec();
    spec.setAmount(split.getAmount());
    return spec;
  }
}
