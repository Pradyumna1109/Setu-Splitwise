package com.example.setu_spliwise.common.aspects;

import com.example.setu_spliwise.exceptions.ForbiddenException;
import com.example.setu_spliwise.services.GroupService;
import com.example.setu_spliwise.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

@Aspect
@Component
@Slf4j
public class AuthorizationAspect {
  private JwtUtil jwtUtil;
  private GroupService groupService;

  public AuthorizationAspect(
      JwtUtil jwtUtil, HttpServletRequest request, GroupService groupService) {
    this.jwtUtil = jwtUtil;
    this.request = request;
    this.groupService = groupService;
  }

  private HttpServletRequest request;

  @Around("@annotation(com.example.setu_spliwise.common.GroupAuthorize)")
  public Object validateToken(ProceedingJoinPoint joinPoint) throws Throwable {
    Map<String, Object> pathVariables = new HashMap<>();
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Object[] args = joinPoint.getArgs();
    Parameter[] parameters = signature.getMethod().getParameters();

    for (int i = 0; i < parameters.length; i++) {
      if (parameters[i].isAnnotationPresent(PathVariable.class)) {
        pathVariables.put(parameters[i].getName(), args[i]);
        log.info(parameters[i].getName());
      }
    }

    if (pathVariables.containsKey("groupId")) {

      groupService.findGroupById((UUID) pathVariables.get("groupId"));
      log.info("{}", (UUID) pathVariables.get("groupId"));
      if (groupService.isUserInGroup(
          (UUID) pathVariables.get("groupId"), UUID.fromString(MDC.get("user-id")))) {
        return joinPoint.proceed();
      }
    }

    throw new ForbiddenException("User Forbidden");
  }
}
