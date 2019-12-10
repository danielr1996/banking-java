package de.danielr1996.banking.infrastructure.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.security.Key;

@Configuration
@Slf4j
public class SecretKeyProvider {
  public static final String SIGN_KEY_TOKEN = "SIGN_KEY_TOKEN";

  // FIXME: Load key from file
  @Bean
  @Qualifier(SIGN_KEY_TOKEN)
  public Key getSigningKey() {
    try {
      ObjectInputStream ois = new ObjectInputStream(SecretKeyProvider.class.getClassLoader().getResourceAsStream("encryption/key.ser"));
      return (Key) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      log.error("Cannot deserialize signing key, generating new key");
      return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }
  }
}
