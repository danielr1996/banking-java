package de.danielr1996.banking.infrastructure.security;

import de.danielr1996.banking.application.auth.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.sql.Date;
import java.time.Instant;

import static de.danielr1996.banking.infrastructure.security.SecretKeyProvider.JWT_SECRET_KEY;

@Service
@Slf4j
public class TokenGenerator {
  private SecretKey signingKey;

  public TokenGenerator(@Autowired
                        @Qualifier(JWT_SECRET_KEY) SecretKey secretKey) {
    this.signingKey = secretKey;
  }

  public String generate(User user) {
    // FIXME: kid sicher ermitteln
//    String keyId = Base64.getEncoder().encodeToString(this.signingKey.getEncoded());
    return Jwts.builder()
//      .setHeaderParam("kid",keyId)
      .setIssuer("banking-service")
      .setIssuedAt(Date.from(Instant.now()))
      .setSubject(user.getName())
      .signWith(this.signingKey)
      .compact();
  }
}
