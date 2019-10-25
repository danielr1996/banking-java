package de.danielr1996.banking.infrastructure.websockets;

import java.lang.reflect.Type;
import de.danielr1996.banking.infrastructure.tasks.ImportTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Controller
@Slf4j
public class RefreshController {

  @MessageMapping("/refresh")
  @SendTo("/topic/passwort")
  public String refresh(RefreshRequest refreshRequest) {
    return null;
  }
}
