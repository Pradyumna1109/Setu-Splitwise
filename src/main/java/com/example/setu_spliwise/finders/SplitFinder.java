package com.example.setu_spliwise.finders;

import com.example.setu_spliwise.models.Expense;
import com.example.setu_spliwise.models.Split;
import io.ebean.DB;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SplitFinder extends BaseFinder<Split> {

  private final UserFinder userFinder;

  @Autowired
  public SplitFinder(UserFinder userFinder) {
    super(Split.class);
    this.userFinder = userFinder;
  }

  public Split createSplit(double amount, double percentage, UUID userId, Expense expense) {
    Split split = new Split();
    split.setAmount(amount);
    split.setPercentage(percentage);
    split.setUser(userFinder.get(userId));
    split.setExpense(expense);
    if (expense.getUserGroup().getUser().getId().equals(userId) || amount == 0 || percentage == 0) {
      split.setPaid(true);
    }
    split.save();
    return split;
  }

  public void markAsPaid(List<UUID> expenseIds, UUID userId) {
    DB.update(Split.class)
        .set("is_paid", true)
        .where()
        .eq("user.id", userId)
        .in("expense_id", expenseIds)
        .update();
  }

  public List<Split> findUnpaidSplitsByUserAndGroup(UUID userId, UUID groupId) {
    return query()
        .fetch("expense")
        .where()
        .eq("user.id", userId)
        .eq("expense.userGroup.group.id", groupId)
        .eq("isPaid", false)
        .findList();
  }

  public List<Split> findUnpaidSplitsForExpensesPaidByUser(UUID userId, UUID groupId) {
    return query()
        .fetch("expense")
        .where()
        .eq("expense.userGroup.user.id", userId)
        .eq("expense.userGroup.group.id", groupId)
        .eq("isPaid", false)
        .findList();
  }

  public Optional<Split> getSplitByExpenseId(UUID expenseId, UUID userId, UUID groupId) {
    return query()
        .fetch("expense")
        .where()
        .eq("user.id", userId)
        .eq("expense.userGroup.group.id", groupId)
        .eq("isPaid", false)
        .eq("expense.id", expenseId)
        .findOneOrEmpty();
  }
}
