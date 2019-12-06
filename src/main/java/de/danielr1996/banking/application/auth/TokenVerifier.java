package de.danielr1996.banking.application.auth;

import static de.danielr1996.banking.application.auth.SecretKeyProvider.SIGN_KEY_TOKEN;
import java.security.Key;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
public class TokenVerifier {
  @Autowired
  @Qualifier(SIGN_KEY_TOKEN)
  private Key key;
  private String jwt;
  private JwtParser parser;

  public TokenVerifier(String jwt) {
    this.jwt = jwt;
    this.parser = Jwts.parser()
      .setSigningKey(key)
      .requireIssuer("banking-service");
  }

  public Claims verify(){
    return Jwts.parser().parseClaimsJws(this.jwt).getBody();
  }

  public TokenVerifier requireUser(String username) {
    parser.requireSubject(username);
    return this;
  }

  @Service
  public static class TokenVerifierFactory {
    public TokenVerifier ofJwt(String jwt) {
      return new TokenVerifier(jwt);
    }
  }
}
