package com.example.setu_spliwise.services;

import com.example.setu_spliwise.dtos.spec.SettlementSpec;
import com.example.setu_spliwise.exceptions.BadRequestException;
import com.example.setu_spliwise.finders.ExpenseFinder;
import com.example.setu_spliwise.finders.GroupFinder;
import com.example.setu_spliwise.finders.SplitFinder;
import com.example.setu_spliwise.finders.UserFinder;
import com.example.setu_spliwise.models.Expense;
import com.example.setu_spliwise.models.Group;
import com.example.setu_spliwise.models.Split;
import com.example.setu_spliwise.models.User;
import io.ebean.annotation.Transactional;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SimpleSettlementService extends SettlementService {
  private final ExpenseService expenseService;
  private final ExpenseFinder expenseFinder;
  private final SplitFinder splitFinder;
  private final GroupFinder groupFinder;
  private final UserFinder userFinder;

  @Autowired
  public SimpleSettlementService(
      ExpenseService expenseService,
      ExpenseFinder expenseFinder,
      SplitFinder splitFinder,
      GroupFinder groupFinder,
      UserFinder userFinder) {

    this.expenseService = expenseService;
    this.expenseFinder = expenseFinder;
    this.splitFinder = splitFinder;
    this.groupFinder = groupFinder;
    this.userFinder = userFinder;
  }

  @Transactional
  @Override
  public void settleExpenses(SettlementSpec spec, UUID userId, UUID groupId) {
    List<UUID> expenseIds = new ArrayList<>();
    if (spec.getExpenseId() != null) {
      Expense expense = expenseService.findExpenseById(spec.getExpenseId(), groupId);
      Split split = splitFinder.getSplitByExpenseId(spec.getExpenseId(), userId, groupId).get();
      if (split.isPaid()) {
        throw new BadRequestException("Expense is already paid");
      }
      expenseIds.add(spec.getExpenseId());
    } else {
      List<UUID> expenses =
          expenseService.findAllExpensesByUserAndGroupId(userId, groupId).stream()
              .map(expense -> expense.getId())
              .toList();
      expenseIds.addAll(expenses);
    }
    log.error("expenseIds {}", expenseIds);
    expenseService.markAsPaid(expenseIds, userId);
  }

  @Override
  public Map<UUID, Double> checkBalanceForUser(UUID userId, UUID groupId) {
    Group group = groupFinder.get(groupId);
    User user = userFinder.get(userId);
    List<Split> userReceivalbeSplits =
        splitFinder.findUnpaidSplitsForExpensesPaidByUser(userId, groupId);
    List<Split> userPayableSplits = splitFinder.findUnpaidSplitsByUserAndGroup(userId, groupId);
    Map<UUID, Double> balanceMap = new HashMap<>();

    userReceivalbeSplits.forEach(
        split -> {
          if (!balanceMap.containsKey(split.getUser().getId())) {
            balanceMap.put(split.getUser().getId(), split.getAmount());
          } else {
            balanceMap.put(
                split.getUser().getId(),
                balanceMap.get(split.getUser().getId()) + split.getAmount());
          }
        });

    userPayableSplits.forEach(
        split -> {
          if (!balanceMap.containsKey(split.getExpense().getUserGroup().getUser().getId())) {
            balanceMap.put(
                split.getExpense().getUserGroup().getUser().getId(), 0 - split.getAmount());
          } else {
            balanceMap.put(
                split.getExpense().getUserGroup().getUser().getId(),
                balanceMap.get(split.getExpense().getUserGroup().getUser().getId())
                    - split.getAmount());
          }
        });

    return balanceMap;
  }
}
