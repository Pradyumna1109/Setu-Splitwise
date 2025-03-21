package com.example.setu_spliwise.util;

import com.example.setu_spliwise.dtos.enums.ExpenseType;
import com.example.setu_spliwise.exceptions.BadRequestException;
import com.example.setu_spliwise.util.expenseUtil.EqualSplitStrategy;
import com.example.setu_spliwise.util.expenseUtil.PercentageSplitStrategy;
import com.example.setu_spliwise.util.expenseUtil.SplitStrategy;
import com.example.setu_spliwise.util.expenseUtil.UnequalSplitStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SplitUtil {
  private final EqualSplitStrategy equalSplitStrategy;
  private final UnequalSplitStrategy unequalSplitStrategy;
  private final PercentageSplitStrategy percentageSplitStrategy;

  @Autowired
  public SplitUtil(
      EqualSplitStrategy equalSplitStrategy,
      UnequalSplitStrategy unequalSplitStrategy,
      PercentageSplitStrategy percentageSplitStrategy) {
    this.equalSplitStrategy = equalSplitStrategy;
    this.unequalSplitStrategy = unequalSplitStrategy;
    this.percentageSplitStrategy = percentageSplitStrategy;
  }

  public SplitStrategy splitStrategy(ExpenseType type) {
    try {
      SplitStrategy strategy =
          switch (type) {
            case EQUAL -> equalSplitStrategy;
            case UNEQUAL -> unequalSplitStrategy;
            case PERCENTAGE -> percentageSplitStrategy;
            default -> throw new BadRequestException("Invalid expense type");
          };
      return strategy;
    } catch (BadRequestException e) {
      throw new BadRequestException(e.getMessage());
    }
  }
}
