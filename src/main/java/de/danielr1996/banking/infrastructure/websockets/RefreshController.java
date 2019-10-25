package de.danielr1996.banking.infrastructure.websockets;

import de.danielr1996.banking.infrastructure.tasks.ImportTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class RefreshController {

  @Autowired
  ImportTask importTask;

  @MessageMapping("/hello")
  @SendTo("/topic/refresh")
  public void refresh() {
   /* if (refreshRequest.getTan() != null && refreshRequest.getTanmedium() != null) {
      importTask.importIntoDb(refreshRequest::getTan, refreshRequest::getTanmedium);
    }else{

    }*/
  }
}
