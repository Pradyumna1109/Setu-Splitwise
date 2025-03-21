package com.example.setu_spliwise.util.expenseUtil;

import com.example.setu_spliwise.dtos.spec.*;
import com.example.setu_spliwise.exceptions.BadRequestException;
import com.example.setu_spliwise.finders.SplitFinder;
import com.example.setu_spliwise.models.Expense;
import com.example.setu_spliwise.models.Split;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class PercentageSplitStrategy implements SplitStrategy {

  private final SplitFinder splitFinder;

  public PercentageSplitStrategy(SplitFinder splitFinder) {
    this.splitFinder = splitFinder;
  }

  @Override
  public void validate(double amount, Set<SplitSpec> splits) {

    if (splits == null || splits.isEmpty()) {
      throw new BadRequestException("Splits cannot be empty");
    }

    BigDecimal totalPercentage = BigDecimal.ZERO;
    BigDecimal expectedPercentage = BigDecimal.valueOf(100);

    for (SplitSpec split : splits) {
      PercentageSplitSpec splitSpec = (PercentageSplitSpec) split;
      totalPercentage = totalPercentage.add(BigDecimal.valueOf(splitSpec.getPercentage()));
    }

    BigDecimal difference = totalPercentage.subtract(expectedPercentage).abs();

    if (difference.compareTo(TOLERANCE) > 0) {
      throw new BadRequestException(
          String.format(
              "For EXACT split type, the sum of split amounts (%.2f) must equal the expense amount (%.2f)",
              totalPercentage.setScale(2, RoundingMode.HALF_UP),
              expectedPercentage.setScale(2, RoundingMode.HALF_UP)));
    }
  }

  @Override
  public Set<Split> createSplit(ExpenseSpec expenseSpec, Expense expense) {
    return expenseSpec.getSplits().stream()
        .map(
            split -> {
              PercentageSplitSpec splitSpec = (PercentageSplitSpec) split;
              double amount = (splitSpec.getPercentage() * expenseSpec.getAmount()) / (double) 100;
              BigDecimal bd = BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);
              double result = bd.doubleValue();
              return splitFinder.createSplit(
                  amount, splitSpec.getPercentage(), splitSpec.getUserId(), expense);
            })
        .collect(Collectors.toSet());
  }

  @Override
  public SplitSpec getSplitSpec(Split split) {
    PercentageSplitSpec spec = new PercentageSplitSpec();
    spec.setPercentage(split.getPercentage());
    return spec;
  }
}
