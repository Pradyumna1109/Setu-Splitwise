package com.example.setu_spliwise.services;

import com.example.setu_spliwise.dtos.enums.ExpenseType;
import com.example.setu_spliwise.dtos.spec.ExpenseSpec;
import com.example.setu_spliwise.dtos.spec.SplitSpec;
import com.example.setu_spliwise.exceptions.BadRequestException;
import com.example.setu_spliwise.finders.*;
import com.example.setu_spliwise.models.*;
import com.example.setu_spliwise.util.SplitUtil;
import com.example.setu_spliwise.util.expenseUtil.SplitStrategy;
import io.ebean.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

  private final ExpenseFinder expenseFinder;
  private final SplitFinder splitFinder;
  private final UserGroupFinder userGroupFinder;
  private final GroupFinder groupFinder;
  private final UserService userService;
  private final SplitUtil splitUtil;

  @Autowired
  public ExpenseService(
      ExpenseFinder expenseFinder,
      SplitFinder splitFinder,
      UserGroupFinder userGroupFinder,
      GroupFinder groupFinder,
      UserService userService,
      SplitUtil splitUtil) {
    this.expenseFinder = expenseFinder;
    this.splitFinder = splitFinder;
    this.userGroupFinder = userGroupFinder;
    this.groupFinder = groupFinder;
    this.userService = userService;
    this.splitUtil = splitUtil;
  }

  @Transactional
  public Expense create(ExpenseSpec expenseSpec, UUID groupId) {

    validateSplit(expenseSpec.getSplits(), expenseSpec.getType());
    List<UUID> userIds = expenseSpec.getSplits().stream().map(split -> split.getUserId()).toList();
    UUID paidById = UUID.fromString(MDC.get("user-id"));
    Group group = groupFinder.get(groupId);
    List<User> users = userService.validateUsers(userIds);
    SplitStrategy strategy = splitUtil.splitStrategy(expenseSpec.getType());
    validateExpense(expenseSpec, userIds, group, strategy);
    UserGroup userGroup = userGroupFinder.getUserInGroup(groupId, paidById).get();
    Expense expense = expenseFinder.createExpense(expenseSpec, userGroup);
    Set<Split> splits = strategy.createSplit(expenseSpec, expense);
    expense.setSplits(splits);
    return expense;
  }

  private void validateSplit(Set<SplitSpec> split, ExpenseType type) {
    split.forEach(
        splitSpec -> {
          if (!splitSpec.getType().equals(type)) {
            throw new BadRequestException("Expense type should be the same in all splits");
          }
        });
  }

  private void validateExpense(
      ExpenseSpec expenseSpec, List<UUID> userIds, Group group, SplitStrategy strategy) {
    try {
      Set<UUID> groupMemberIds =
          group.getUsers().stream().map(User::getId).collect(Collectors.toSet());

      List<UUID> nonMemberIds =
          userIds.stream().filter(id -> !groupMemberIds.contains(id)).toList();

      if (!nonMemberIds.isEmpty()) {
        throw new BadRequestException("Users not part of the group: " + nonMemberIds);
      }

      if (!groupMemberIds.contains(UUID.fromString(MDC.get("user-id")))) {
        throw new BadRequestException("Payer must be a member of the group");
      }

      strategy.validate(expenseSpec.getAmount(), expenseSpec.getSplits());
    } catch (BadRequestException e) {
      throw new BadRequestException(e.getMessage());
    }
  }

  public List<Expense> findAllExpensesByUser(UUID userId) {
    return expenseFinder.findAllExpensesByUser(userId);
  }

  public List<Expense> findAllExpensesByUserAndGroupId(UUID userId, UUID groupId) {
    return expenseFinder.findAllExpensesByUserAndGroupId(userId, groupId);
  }

  public Expense findExpenseById(UUID id, UUID groupId) {
    UUID userId = UUID.fromString(MDC.get("user-id"));
    return expenseFinder.findExpenseById(id, userId, groupId);
  }

  public void markAsPaid(List<UUID> expenseIds, UUID userId) {
    splitFinder.markAsPaid(expenseIds, userId);
  }
}
