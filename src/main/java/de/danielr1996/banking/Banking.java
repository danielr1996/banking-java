package de.danielr1996.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.security.KeyStore;

@SpringBootApplication
@EnableScheduling
@EnableResourceServer
public class Banking {
  public static void main(String[] args) {
//    KeyStore.Entry newEntry = new KeyStore.TrustedCertificateEntry();

    SpringApplication.run(Banking.class, args);
  }

  @Configuration
  static class OktaOAuth2WebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http
//        .authorizeRequests()
//        .anyRequest()
//        .authenticated()
//        .and()
        .oauth2ResourceServer().jwt();
    }
  }
}
