package de.danielr1996.banking.application.auth;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Key;

@Configuration
public class SecretKeyProvider {
  public static final String SIGN_KEY_TOKEN = "SIGN_KEY_TOKEN";

  @Bean
  @Qualifier(SIGN_KEY_TOKEN)
  public Key getSigningKey() {
    return Keys.secretKeyFor(SignatureAlgorithm.HS256);
  }
}
