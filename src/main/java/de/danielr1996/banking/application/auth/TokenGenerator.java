package de.danielr1996.banking.application.auth;

import static de.danielr1996.banking.application.auth.SecretKeyProvider.SIGN_KEY_TOKEN;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.sql.Date;
import java.time.Instant;
import java.util.Base64;

@Service
@Slf4j
public class TokenGenerator {

  @Autowired
  @Qualifier(SIGN_KEY_TOKEN)
  Key signingKey;

  public String generate(User user) {
    String keyId = Base64.getEncoder().encodeToString(this.signingKey.getEncoded());
    return Jwts.builder()
      .setHeaderParam("kid",keyId)
      .setIssuer("banking-service")
      .setIssuedAt(Date.from(Instant.now()))
      .setSubject(user.getName())
      .signWith(this.signingKey)
      .compact();
  }
}
