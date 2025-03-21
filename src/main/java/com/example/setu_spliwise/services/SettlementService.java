package com.example.setu_spliwise.services;

import com.example.setu_spliwise.dtos.spec.SettlementSpec;
import java.util.Map;
import java.util.UUID;

public abstract class SettlementService {
  public abstract void settleExpenses(SettlementSpec settlementSpec, UUID userId, UUID groupId);

  public abstract Map<UUID, Double> checkBalanceForUser(UUID userId, UUID groupId);
}
