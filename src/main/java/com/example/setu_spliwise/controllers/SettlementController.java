package com.example.setu_spliwise.controllers;

import com.example.setu_spliwise.common.Authenticate;
import com.example.setu_spliwise.common.GroupAuthorize;
import com.example.setu_spliwise.dtos.BalanceResponseDto;
import com.example.setu_spliwise.dtos.SettlementDto;
import com.example.setu_spliwise.dtos.spec.SettlementSpec;
import com.example.setu_spliwise.services.SettlementService;
import com.example.setu_spliwise.util.ApiUtil;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups/{groupId}/settlements")
public class SettlementController {

  private final SettlementService settlementService;
  private final ApiUtil apiUtil;

  public SettlementController(SettlementService settlementService, ApiUtil apiUtil) {
    this.settlementService = settlementService;
    this.apiUtil = apiUtil;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  @Authenticate
  @GroupAuthorize
  public ResponseEntity<Void> createSettlement(
      @Valid @PathVariable(name = "groupId") UUID groupId,
      @RequestBody @Valid SettlementDto settlementDto) {
    SettlementSpec spec = settlementDto.getSettlementSpec();
    UUID userId = UUID.fromString(MDC.get("user-id"));
    settlementService.settleExpenses(spec, userId, groupId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Authenticate
  @GroupAuthorize
  public ResponseEntity<BalanceResponseDto> checkBalanceForUser(@Valid @PathVariable UUID groupId) {
    UUID userId = UUID.fromString(MDC.get("user-id"));
    Map<UUID, Double> balanceMap = settlementService.checkBalanceForUser(userId, groupId);
    BalanceResponseDto balanceResponseDto = apiUtil.getBalanceResponseDto(balanceMap);
    return new ResponseEntity<>(balanceResponseDto, HttpStatus.CREATED);
  }
}
