package de.danielr1996.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableScheduling
public class Banking {
//  @Autowired
//  ChatServer chatServer;

  public static void main(String[] args) {

    SpringApplication.run(Banking.class, args);
  }

  /*@Configuration
  static class OktaOAuth2WebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests()
        .antMatchers("/graphql").permitAll()
        .anyRequest().authenticated()
        .and().oauth2Client()
        .and().oauth2Login();
    }
  }*/
}
