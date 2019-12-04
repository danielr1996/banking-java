package de.danielr1996.banking;

import ch.rasc.wamp2spring.servlet.EnableServletWamp;
import ch.rasc.wamp2spring.servlet.WampServletConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;

@SpringBootApplication
//@EnableScheduling
@EnableServletWamp
public class Banking implements WampServletConfigurer {
//  @Autowired
//  ChatServer chatServer;

  public static void main(String[] args) {

    SpringApplication.run(Banking.class, args);
  }

  @Override
  public void configureWebSocketHandlerRegistration(
    WebSocketHandlerRegistration registration) {
    registration.setAllowedOrigins("*");
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
