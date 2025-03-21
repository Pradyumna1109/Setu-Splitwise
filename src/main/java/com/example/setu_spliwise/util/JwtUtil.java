package com.example.setu_spliwise.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  private static final String SECRET_KEY = "wJz3f4Rc6g6h9XZ27P0HgGtXKcN9dZnK3mYpxyjJzzg=";

  public static SecretKey getSecretKey() {
    byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(decodedKey);
  }

  // Generate JWT Token with userId (Optional to validate later)
  public String generateToken(UUID userId) {
    return Jwts.builder()
        .setSubject(String.valueOf(userId))
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiration
        .signWith(getSecretKey())
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
      return true; // Token is valid
    } catch (ExpiredJwtException e) {
      System.out.println("Token expired: " + e.getMessage());
    } catch (SignatureException e) {
      System.out.println("Invalid signature: " + e.getMessage());
    } catch (Exception e) {
      System.out.println("Invalid token: " + e.getMessage());
    }
    return false;
  }

  // Extract UserId (Optional if needed)
  public UUID extractUserId(String token) {
    Claims claims =
        Jwts.parser().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody();
    return UUID.fromString(claims.getSubject());
  }
}
