package de.danielr1996.banking.infrastructure.wamp;


import ch.rasc.wamp2spring.servlet.EnableServletWamp;
import ch.rasc.wamp2spring.servlet.WampServletConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;

@EnableServletWamp
@Component
public class WampConfiguration implements WampServletConfigurer {
  @Override
  public void configureWebSocketHandlerRegistration(WebSocketHandlerRegistration registration) {
    registration.setAllowedOrigins("*");
  }
}
