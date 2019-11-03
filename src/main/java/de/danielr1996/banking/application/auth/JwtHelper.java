package de.danielr1996.banking.application.auth;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtHelper {
  Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  public String generateJwt(User user) {
    return Jwts.builder().setSubject(user.getName()).signWith(key).compact();
  }

  public boolean validate(String jwt) {
    try {
      Jwts.parser().setSigningKey(key).parseClaimsJws(jwt);
      return false;
    } catch (JwtException e) {
      return false;
    }
  }
}
