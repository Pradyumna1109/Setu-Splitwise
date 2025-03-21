package com.example.setu_spliwise.util;

import com.example.setu_spliwise.dtos.*;
import com.example.setu_spliwise.dtos.data.*;
import com.example.setu_spliwise.dtos.enums.BalanceType;
import com.example.setu_spliwise.dtos.info.*;
import com.example.setu_spliwise.dtos.spec.*;
import com.example.setu_spliwise.finders.UserFinder;
import com.example.setu_spliwise.models.Expense;
import com.example.setu_spliwise.models.Group;
import com.example.setu_spliwise.models.User;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApiUtil {

  private final SplitUtil splitUtil;
  private final UserFinder userFinder;

  @Autowired
  public ApiUtil(SplitUtil splitUtil, UserFinder userFinder) {
    this.splitUtil = splitUtil;
    this.userFinder = userFinder;
  }

  public static UserResponseDto getUserResponseDto(User user) {
    UserResponseDto userResponseDto = new UserResponseDto();

    UserSpec userSpec = new UserSpec();
    userSpec.setName(user.getName());
    userSpec.setEmail(user.getEmail());
    UserInfo userInfo = new UserInfo();
    userInfo.setUserId(user.getId());
    UserData userData = new UserData();
    log.info(user.getEmail());
    userData.setUserInfo(userInfo);
    userData.setUserSpec(userSpec);
    userResponseDto.setUserData(userData);
    return userResponseDto;
  }

  public static GroupResponseDto getGroupResponseDto(Group group) {
    GroupResponseDto groupResponseDto = new GroupResponseDto();
    GroupSpec groupSpec = new GroupSpec();
    groupSpec.setName(group.getName());
    groupSpec.setDescription(group.getDescription());
    GroupInfo groupInfo = new GroupInfo();
    Set<UserData> userData =
        group.getUsers().stream()
            .map(user -> getUserResponseDto(user).getUserData())
            .collect(Collectors.toSet());
    groupInfo.setGroupId(group.getId());
    groupInfo.setUserData(userData);
    GroupData groupData = new GroupData();
    groupData.setGroupInfo(groupInfo);
    groupData.setGroupSpec(groupSpec);
    groupResponseDto.setGroupData(groupData);
    return groupResponseDto;
  }

  public ExpenseResponseDto getExpenseResponseDto(Expense expense) {
    ExpenseResponseDto expenseResponseDto = new ExpenseResponseDto();
    ExpenseData expenseData = new ExpenseData();
    ExpenseSpec expenseSpec = new ExpenseSpec();
    ExpenseInfo expenseInfo = new ExpenseInfo();
    expenseInfo.setExpenseId(expense.getId());
    expenseInfo.setPaidById(expense.getUserGroup().getUser().getId());
    expenseSpec.setDescription(expense.getDescription());
    expenseSpec.setType(expense.getType());
    expenseSpec.setAmount(expense.getAmount());
    Set<SplitInfo> splitInfos =
        expense.getSplits().stream()
            .map(
                split -> {
                  SplitInfo splitInfo = new SplitInfo();
                  splitInfo.setSplitId(split.getId());
                  splitInfo.setAmount(split.getAmount());
                  splitInfo.setPercentage(split.getPercentage());
                  splitInfo.setUserId(split.getUser().getId());
                  return splitInfo;
                })
            .collect(Collectors.toSet());
    expenseSpec.setSplitInfo(splitInfos);
    expenseData.setExpenseInfo(expenseInfo);
    expenseData.setExpenseSpec(expenseSpec);
    expenseResponseDto.setExpenseData(expenseData);
    return expenseResponseDto;
  }

  public static AuthResponseDto getAuthResponseDto(AuthSpec spec, String token) {
    AuthResponseDto authResponseDto = new AuthResponseDto();
    AuthData authData = new AuthData();

    AuthInfo authInfo = new AuthInfo();
    authInfo.setToken(token);
    authData.setAuthInfo(authInfo);
    authData.setAuthSpec(spec);
    authResponseDto.setAuthData(authData);
    return authResponseDto;
  }

  public BalanceResponseDto getBalanceResponseDto(Map<UUID, Double> balanceMap) {
    BalanceResponseDto balanceResponseDto = new BalanceResponseDto();
    BalanceData balanceData = new BalanceData();

    List<BalanceInfo> balanceInfoList =
        balanceMap.entrySet().stream()
            .map(
                entry -> {
                  return new BalanceInfo(
                      userFinder.get(entry.getKey()).getName(),
                      Math.abs(entry.getValue()),
                      entry.getValue() > 0 ? BalanceType.RECEIVABLE : BalanceType.PAYABLE);
                })
            .toList();

    balanceData.setBalanceInfo(balanceInfoList);
    balanceResponseDto.setBalanceData(balanceData);
    return balanceResponseDto;
  }
}
