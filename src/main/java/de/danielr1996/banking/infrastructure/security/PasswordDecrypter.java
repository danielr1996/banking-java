package de.danielr1996.banking.infrastructure.security;

import static de.danielr1996.banking.infrastructure.security.SecretKeyProvider.PASSWORD_SECRET_KEY;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
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
public class PasswordDecrypter {
  private SecretKey secretKey;

  public PasswordDecrypter(@Autowired @Qualifier(PASSWORD_SECRET_KEY) SecretKey secretKey) {
    this.secretKey = secretKey;
  }

  public String decrypt(String password) {
    try {
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.DECRYPT_MODE, secretKey);
      byte[] encrypted = Base64.getDecoder().decode(password);
      byte[] decrypted = cipher.doFinal(encrypted);
      return new String(decrypted);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
      e.printStackTrace();
      return null;
    }
  }
}
