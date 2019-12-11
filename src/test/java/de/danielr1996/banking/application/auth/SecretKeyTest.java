package de.danielr1996.banking.application.auth;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * https://gist.github.com/webtobesocial/5313b0d7abc25e06c2d78f8b767d4bc3
 */
public class SecretKeyTest {

  @Test
  public void testPublicAsymmtricKey() throws Exception {
    //Generate
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);
    KeyPair keyPair = keyPairGenerator.generateKeyPair();
    Key key = keyPair.getPublic();

    //Write
    FileOutputStream fos = new FileOutputStream(new File("public.pem"));
    fos.write("-----BEGIN PUBLIC KEY-----\n".getBytes());
    fos.write(Base64.getMimeEncoder(64, System.getProperty("line.separator").getBytes()).encode(key.getEncoded()));
    fos.write("\n-----END PUBLIC KEY-----\n".getBytes());
    fos.flush();
    fos.close();

    //Read
    String fileContent = Files.readAllLines(new File("public.pem").toPath()).stream().collect(Collectors.joining("\n"));
    String file = fileContent
      .replaceAll("-----BEGIN PUBLIC KEY-----\n", "")
      .replaceAll("\n-----END PUBLIC KEY-----", "");
    X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getMimeDecoder().decode(file));
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PublicKey publicKey = keyFactory.generatePublic(spec);

    assertThat(publicKey.getEncoded(), is(key.getEncoded()));
  }

  @Test
  public void testPrivateAsymmtricKey() throws Exception {
    //Generate
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);
    KeyPair keyPair = keyPairGenerator.generateKeyPair();
    Key key = keyPair.getPrivate();

    //Write
    FileOutputStream fos = new FileOutputStream(new File("private.pem"));

    fos.write("-----BEGIN PRIVATE KEY-----\n".getBytes());
    fos.write(Base64.getMimeEncoder(64, System.getProperty("line.separator").getBytes()).encode(key.getEncoded()));
    fos.write("\n-----END PRIVATE KEY-----\n".getBytes());
    fos.flush();
    fos.close();

    //Read
    String fileContent = Files.readAllLines(new File("private.pem").toPath()).stream().collect(Collectors.joining("\n"));
    String file = fileContent
      .replaceAll("-----BEGIN PRIVATE KEY-----\n", "")
      .replaceAll("\n-----END PRIVATE KEY-----", "");
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getMimeDecoder().decode(file));
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PrivateKey privateKey = keyFactory.generatePrivate(spec);

    assertThat(privateKey.getEncoded(), is(key.getEncoded()));
  }

  @Test
  public void testSymmtricKey() throws Exception {
    //Generate
    SecureRandom random = new SecureRandom();
    byte[] secret = new byte[200];
    random.nextBytes(secret);

    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    SecretKey key = new SecretKeySpec(secret, "HmacSHA256");
    sha256_HMAC.init(key);

    //Write
    FileOutputStream fos = new FileOutputStream(new File("secret.pem"));

    fos.write("-----BEGIN SECRET KEY-----\n".getBytes());
    fos.write(Base64.getMimeEncoder(64, System.getProperty("line.separator").getBytes()).encode(key.getEncoded()));
    fos.write("\n-----END SECRET KEY-----\n".getBytes());
    fos.flush();
    fos.close();

    //Read
    String fileContent = Files.readAllLines(new File("secret.pem").toPath()).stream().collect(Collectors.joining("\n"));
    String file = fileContent
      .replaceAll("-----BEGIN SECRET KEY-----\n", "")
      .replaceAll("\n-----END SECRET KEY-----", "");
    SecretKey spec = new SecretKeySpec(Base64.getMimeDecoder().decode(file), "HmacSHA256");

    assertThat(spec.getEncoded(), is(key.getEncoded()));
  }
}
