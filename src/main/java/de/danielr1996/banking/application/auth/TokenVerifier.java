package de.danielr1996.banking.application.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.security.Key;

import static de.danielr1996.banking.application.auth.SecretKeyProvider.SIGN_KEY_TOKEN;

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
    @Autowired
    @Qualifier(SIGN_KEY_TOKEN)
    private Key key;

    public TokenVerifier ofJwt(String jwt) {
      return new TokenVerifier(this.key, jwt);
    }
  }
}
