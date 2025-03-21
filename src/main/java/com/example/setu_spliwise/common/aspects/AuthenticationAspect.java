package com.example.setu_spliwise.common.aspects;

import com.example.setu_spliwise.exceptions.UnAuthorisedException;
import com.example.setu_spliwise.finders.UserFinder;
import com.example.setu_spliwise.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthenticationAspect {
  private JwtUtil jwtUtil;
  private UserFinder userFinder;

  public AuthenticationAspect(JwtUtil jwtUtil, HttpServletRequest request, UserFinder userFinder) {
    this.jwtUtil = jwtUtil;
    this.request = request;
    this.userFinder = userFinder;
  }

  private HttpServletRequest request;

  @Around("@annotation(com.example.setu_spliwise.common.Authenticate)")
  public Object validateToken(ProceedingJoinPoint joinPoint) throws Throwable {
    String token = request.getHeader("Authorization");

    if (token == null || !token.startsWith("Bearer ")) {
      throw new UnAuthorisedException("Missing or invalid token");
    }

    token = token.substring(7); // Remove "Bearer " prefix

    if (!jwtUtil.validateToken(token)) {
      throw new UnAuthorisedException("Invalid token");
    }

    String userId = String.valueOf(jwtUtil.extractUserId(token));

    userFinder.get(UUID.fromString(userId));

    MDC.put("user-id", userId);

    // Proceed if token is valid
    return joinPoint.proceed();
  }
}
