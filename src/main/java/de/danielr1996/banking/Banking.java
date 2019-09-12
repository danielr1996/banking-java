package de.danielr1996.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

import java.security.KeyStore;

@SpringBootApplication
@EnableScheduling
@EnableResourceServer
@EnableOAuth2Sso
public class Banking {
  public static void main(String[] args) {
//    KeyStore.Entry newEntry = new KeyStore.TrustedCertificateEntry();

    SpringApplication.run(Banking.class, args);
  }
}
