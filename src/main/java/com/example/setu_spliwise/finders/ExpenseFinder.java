package com.example.setu_spliwise.finders;

import com.example.setu_spliwise.dtos.spec.ExpenseSpec;
import com.example.setu_spliwise.exceptions.NotFoundException;
import com.example.setu_spliwise.models.Expense;
import com.example.setu_spliwise.models.UserGroup;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class ExpenseFinder extends BaseFinder<Expense> {

  public ExpenseFinder() {
    super(Expense.class);
  }

  public Expense createExpense(ExpenseSpec spec, UserGroup userGroup) {
    Expense expense = new Expense();
    expense.setDescription(spec.getDescription());
    expense.setAmount(spec.getAmount());
    expense.setType(spec.getType());
    expense.setUserGroup(userGroup);
    expense.save();
    return expense;
  }

  public List<Expense> findAllExpensesByUser(UUID userId) {
    return query().fetch("userGroup").where().eq("userGroup.user.id", userId).findList();
  }

  public List<Expense> findAllExpensesByUserAndGroupId(UUID userId, UUID groupId) {
    return query()
        .fetch("userGroup")
        .where()
        .eq("splits.user.id", userId)
        .eq("userGroup.group.id", groupId)
        .findList();
  }

  public List<Expense> findAllExpensesByUserAndGroupIdNotPaid(UUID userId, UUID groupId) {
    return query()
        .fetch("userGroup")
        .where()
        .eq("splits.user.id", userId)
        .eq("userGroup.group.id", groupId)
        .eq("splits.isPaid", false)
        .findList();
  }

  public Expense findExpenseById(UUID id, UUID userId, UUID groupId) {
    Optional<Expense> expense =
        query()
            .fetch("userGroup")
            .where()
            .eq("splits.user.id", userId)
            .eq("userGroup.group.id", groupId)
            .eq("id", id)
            .findOneOrEmpty();
    return expense.orElseThrow(() -> new NotFoundException("Expense not found for id: " + id));
  }
}
