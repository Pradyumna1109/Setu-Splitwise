package com.example.setu_spliwise.util.expenseUtil;

import com.example.setu_spliwise.dtos.spec.ExpenseSpec;
import com.example.setu_spliwise.dtos.spec.SplitSpec;
import com.example.setu_spliwise.models.Expense;
import com.example.setu_spliwise.models.Split;
import java.math.BigDecimal;
import java.util.Set;

public interface SplitStrategy {

  static final BigDecimal TOLERANCE = BigDecimal.valueOf(0.01);

  public void validate(double amount, Set<SplitSpec> splits);

  public Set<Split> createSplit(ExpenseSpec expenseSpec, Expense expense);

  public SplitSpec getSplitSpec(Split split);
}
