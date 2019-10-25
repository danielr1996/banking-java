package de.danielr1996.banking.infrastructure.websockets;

import java.net.URISyntaxException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class PlainWebSocketConfig implements WebSocketConfigurer {
  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    try {
      registry.addHandler(myHandler(), "/banking").setAllowedOrigins("*");
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  @Bean
  public WebSocketHandler myHandler() throws URISyntaxException {
    return new WebSocketHandler();
  }
}
