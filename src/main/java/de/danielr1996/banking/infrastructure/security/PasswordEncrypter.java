package de.danielr1996.banking.infrastructure.security;

import static de.danielr1996.banking.infrastructure.security.SecretKeyProvider.PASSWORD_SECRET_KEY;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncrypter {
  private SecretKey secretKey;

  public PasswordEncrypter(@Autowired @Qualifier(PASSWORD_SECRET_KEY) SecretKey secretKey) {
    this.secretKey = secretKey;
  }

  public String encrypt(String password) {
    try {
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
      byte[] encrypted = cipher.doFinal(password.getBytes());
      return Base64.getEncoder().encodeToString(encrypted);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static void main(String[] args) {
    System.out.println(new PasswordEncrypter(new SecretKeyProvider().getPasswordSecretKey()).encrypt("302222"));
  }
}
