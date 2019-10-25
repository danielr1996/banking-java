package de.danielr1996.banking.infrastructure.websockets;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

@Slf4j
public class ChatServer extends WebSocketServer {
  public ChatServer() throws IOException, InterruptedException {
    super(new InetSocketAddress(1887));
    this.start();
    BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );
    while ( true ) {
      String in = sysin.readLine();
      broadcast( in );
      if( in.equals( "exit" ) ) {
        stop(1000);
        break;
      }
    }
  }

  @Override
  public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
    log.info("open");
  }

  @Override
  public void onClose(WebSocket webSocket, int i, String s, boolean b) {
    log.info("close");
  }

  @Override
  public void onMessage(WebSocket webSocket, String s) {
    log.info("message");
  }

  @Override
  public void onError(WebSocket webSocket, Exception e) {
    log.info("error");
  }

  @Override
  public void onStart() {
    log.info("start");
  }
}
