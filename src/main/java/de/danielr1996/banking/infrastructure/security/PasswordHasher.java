package de.danielr1996.banking.infrastructure.security;

import de.danielr1996.banking.application.auth.Password;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

@Service
@Slf4j
public class PasswordHasher {
  private static final int ITERATIONS = 200000;
  private final static int KEYLENGTH = 256;

  private byte[] hashPassword(final char[] password, final byte[] salt, final int iterations, final int keyLength) {
    try {
      SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
      PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
      SecretKey key = skf.generateSecret(spec);
      return key.getEncoded();
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      log.error("No Algorithm found or keyspec invalid");
      throw new RuntimeException(e);
    }
  }

  public Password hash(final String password) {
    final char[] passwordArray = password.toCharArray();
    final String saltString = getSecureRandom();
    final byte[] saltArray = saltString.getBytes();
    byte[] hashedPassword = hashPassword(passwordArray, saltArray, ITERATIONS, KEYLENGTH);

    return Password.builder()
      .passwordhash(new BigInteger(1, hashedPassword).toString(16))
      .salt(saltString)
      .iterations(ITERATIONS)
      .build();
  }

  public boolean verify(final String password, final Password hashedPassword) {
    final char[] passwordArray = password.toCharArray();
    final int keyLength = 256;
    byte[] hashed = hashPassword(passwordArray, hashedPassword.getSalt().getBytes(), hashedPassword.getIterations(), keyLength);

    return hashedPassword.getPasswordhash().equals(new BigInteger(1, hashed).toString(16));
  }

  private String getSecureRandom() {
    SecureRandom random = new SecureRandom();
    byte[] values = new byte[20];
    random.nextBytes(values);
    BigInteger bigInt = new BigInteger(1, values);
    return bigInt.toString(16);
  }

  public static void main(String[] args) {
    PasswordHasher passwordHasher = new PasswordHasher();
  }
}
