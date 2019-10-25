package de.danielr1996.banking.infrastructure.websockets;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import de.danielr1996.banking.infrastructure.tasks.ImportTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {
  @Autowired
  ImportTask importTask;
  static CountDownLatch countDownLatch = new CountDownLatch(1);
  static String tan;
  ExampleClient client = new ExampleClient(new URI("ws://localhost:8080/banking"));

  public WebSocketHandler() throws URISyntaxException {
    client.connect();
  }

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
    /*log.info("Message received: {}", message.getPayload());
    if (message.getPayload().equals("Refresh")) {
      importTask.importIntoDb(() -> {
        client.send("Password");
        try {
          countDownLatch.await();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }, () -> "P20");
    }
    if(message.getPayload().equals("Password")){

    }*/
  }
}
