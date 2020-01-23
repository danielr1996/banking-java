package de.danielr1996.banking.infrastructure.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
@Slf4j
public class SecretKeyProvider {
  public static final String JWT_SECRET_KEY = "JWT_SECRET_KEY";
  public static final String PASSWORD_SECRET_KEY = "PASSWORD_SECRET_KEY";
  public static final String PASSPORT_SECRET = "PASSPORT_SECRET";

  @Bean
  @Qualifier(JWT_SECRET_KEY)
  public SecretKey getJwtSignKeyPublicFromFile() {
    return loadHmacSha256Key("encryption/jwtsecret.pem");
  }

  @Bean
  @Qualifier(PASSWORD_SECRET_KEY)
  public SecretKey getPasswordSecretKeyFromFile() {
    return loadAES256Key("encryption/passwordsecret.pem");
  }

  @Bean
  @Qualifier(PASSPORT_SECRET)
  public String getPassportSecret() {
    return loadAES256KeyInPlainText("encryption/passportsecret.pem");
  }

  private PrivateKey loadPrivateKey(String classPathLocation) {
    try {
      String fileContent = IOUtils.toString(SecretKeyProvider.class.getClassLoader().getResourceAsStream(classPathLocation), Charset.defaultCharset());
      String file = fileContent
        .replaceAll("-----BEGIN PRIVATE KEY-----", "")
        .replaceAll("-----END PRIVATE KEY-----", "");
      PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getMimeDecoder().decode(file));
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      return keyFactory.generatePrivate(spec);
    } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
      e.printStackTrace();
      return null;
    }
  }

  private PublicKey loadPublicKey(String classPathLocation) {
    try {
      String fileContent = IOUtils.toString(SecretKeyProvider.class.getClassLoader().getResourceAsStream(classPathLocation), Charset.defaultCharset());
      String file = fileContent
        .replaceAll("-----BEGIN PUBLIC KEY-----", "")
        .replaceAll("-----END PUBLIC KEY-----", "");
      X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getMimeDecoder().decode(file));
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      return keyFactory.generatePublic(spec);
    } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
      e.printStackTrace();
      return null;
    }
  }

  private SecretKey loadHmacSha256Key(String classPathLocation) {
    try {
      String fileContent = IOUtils.toString(SecretKeyProvider.class.getClassLoader().getResourceAsStream(classPathLocation), Charset.defaultCharset());
      String file = fileContent
        .replaceAll("-----BEGIN SECRET KEY-----", "")
        .replaceAll("-----END SECRET KEY-----", "");
      return new SecretKeySpec(Base64.getMimeDecoder().decode(file), "HmacSHA256");
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private SecretKey loadAES256Key(String classPathLocation) {
    String file = loadAES256KeyInPlainText(classPathLocation);
    return new SecretKeySpec(Base64.getMimeDecoder().decode(file), "AES");
  }

  private String loadAES256KeyInPlainText(String classPathLocation) {
    try {
      String fileContent = IOUtils.toString(SecretKeyProvider.class.getClassLoader().getResourceAsStream(classPathLocation), Charset.defaultCharset());
      return fileContent
        .replaceAll("-----BEGIN SECRET KEY-----", "")
        .replaceAll("-----END SECRET KEY-----", "");
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
