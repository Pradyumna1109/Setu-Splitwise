package com.example.setu_spliwise.util.expenseUtil;

import com.example.setu_spliwise.dtos.spec.ExpenseSpec;
import com.example.setu_spliwise.dtos.spec.SplitSpec;
import com.example.setu_spliwise.dtos.spec.UnequalSplitSpec;
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
public class UnequalSplitStrategy implements SplitStrategy {
  private final SplitFinder splitFinder;

  @Autowired
  public UnequalSplitStrategy(SplitFinder splitFinder) {
    this.splitFinder = splitFinder;
  }

  @Override
  public void validate(double amount, Set<SplitSpec> splits) {
    if (splits == null || splits.isEmpty()) {
      throw new BadRequestException("Splits cannot be empty");
    }

    BigDecimal totalSplitAmount = BigDecimal.ZERO;
    BigDecimal expectedAmount = BigDecimal.valueOf(amount);

    for (SplitSpec split : splits) {
      UnequalSplitSpec splitSpec = (UnequalSplitSpec) split;
      totalSplitAmount = totalSplitAmount.add(BigDecimal.valueOf(splitSpec.getAmount()));
    }

    BigDecimal difference = totalSplitAmount.subtract(expectedAmount).abs();

    if (difference.compareTo(TOLERANCE) > 0) {
      throw new BadRequestException(
          String.format(
              "For UNEQUAL split type, the sum of split amounts (%.2f) must equal the expense amount (%.2f)",
              totalSplitAmount.setScale(2, RoundingMode.HALF_UP),
              expectedAmount.setScale(2, RoundingMode.HALF_UP)));
    }
  }

  @Override
  public Set<Split> createSplit(ExpenseSpec expenseSpec, Expense expense) {
    return expenseSpec.getSplits().stream()
        .map(
            split -> {
              UnequalSplitSpec splitSpec = (UnequalSplitSpec) split;
              double percentage = ((double) splitSpec.getAmount() / expense.getAmount()) * 100;
              BigDecimal bd = BigDecimal.valueOf(percentage).setScale(2, RoundingMode.HALF_UP);
              double result = bd.doubleValue();
              return splitFinder.createSplit(
                  splitSpec.getAmount(), result, splitSpec.getUserId(), expense);
            })
        .collect(Collectors.toSet());
  }

  @Override
  public SplitSpec getSplitSpec(Split split) {
    UnequalSplitSpec spec = new UnequalSplitSpec();
    spec.setAmount(split.getAmount());
    return spec;
  }
}
