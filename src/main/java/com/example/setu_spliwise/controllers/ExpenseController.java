package com.example.setu_spliwise.controllers;

import com.example.setu_spliwise.common.Authenticate;
import com.example.setu_spliwise.common.GroupAuthorize;
import com.example.setu_spliwise.dtos.CreateExpenseDto;
import com.example.setu_spliwise.dtos.ExpenseResponseDto;
import com.example.setu_spliwise.models.Expense;
import com.example.setu_spliwise.services.ExpenseService;
import com.example.setu_spliwise.util.ApiUtil;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups/{groupId}/expenses")
public class ExpenseController {
  private final ExpenseService expenseService;
  private final ApiUtil apiUtil;

  @Autowired
  public ExpenseController(ExpenseService expenseService, ApiUtil apiUtil) {
    this.expenseService = expenseService;
    this.apiUtil = apiUtil;
  }

  // Create a new expense
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Authenticate
  @GroupAuthorize
  public ResponseEntity<ExpenseResponseDto> createExpense(
      @Valid @PathVariable(name = "groupId") UUID groupId,
      @RequestBody @Valid CreateExpenseDto createExpenseDto) {
    Expense expense = expenseService.create(createExpenseDto.getExpenseSpec(), groupId);
    ExpenseResponseDto expenseResponseDto = apiUtil.getExpenseResponseDto(expense);
    return new ResponseEntity<>(expenseResponseDto, HttpStatus.CREATED);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Authenticate
  @GroupAuthorize
  public ResponseEntity<List<ExpenseResponseDto>> findAllExpensesByUserAndGroupId(
      @Valid @PathVariable(name = "groupId") UUID groupId) {
    UUID userId = UUID.fromString(MDC.get("user-id"));
    List<Expense> expenses = expenseService.findAllExpensesByUserAndGroupId(userId, groupId);
    List<ExpenseResponseDto> expenseResponseDtos =
        expenses.stream().map(expense -> apiUtil.getExpenseResponseDto(expense)).toList();
    return new ResponseEntity<>(expenseResponseDtos, HttpStatus.OK);
  }

  // Get expense by ID
  @GetMapping("/{expenseId}")
  @ResponseStatus(HttpStatus.OK)
  @Authenticate
  @GroupAuthorize
  public ResponseEntity<ExpenseResponseDto> findExpenseById(
      @Valid @PathVariable(name = "expenseId") UUID id,
      @Valid @PathVariable(name = "groupId") UUID groupId) {
    Expense expense = expenseService.findExpenseById(id, groupId);
    ExpenseResponseDto expenseResponseDto = apiUtil.getExpenseResponseDto(expense);
    return new ResponseEntity<>(expenseResponseDto, HttpStatus.OK);
  }
}
