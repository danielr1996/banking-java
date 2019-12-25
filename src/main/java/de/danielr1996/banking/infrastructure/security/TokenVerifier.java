package de.danielr1996.banking.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

import static de.danielr1996.banking.infrastructure.security.SecretKeyProvider.*;

@Slf4j
public class TokenVerifier {
  private String jwt;
  private JwtParser parser;

  public TokenVerifier(Key signingKey, String jwt) {
    this.jwt = jwt;
    this.parser = Jwts.parser()
      .setSigningKey(signingKey)
      .requireIssuer("banking-service");
  }

  public Claims verify() {
    return this.parser.parseClaimsJws(this.jwt).getBody();
  }

  public TokenVerifier requireUser(String username) {
    parser.requireSubject(username);
    return this;
  }

  @Service
  public static class TokenVerifierFactory {
    private SecretKey validationKey;
    public TokenVerifierFactory(@Autowired @Qualifier(JWT_SECRET_KEY) SecretKey publicKey){
      this.validationKey = publicKey;
    }

    public TokenVerifier ofJwt(String jwt) {
      return new TokenVerifier(this.validationKey, jwt);
    }
  }
}
