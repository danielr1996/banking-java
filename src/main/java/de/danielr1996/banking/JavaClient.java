package de.danielr1996.banking;

import io.crossbar.autobahn.wamp.Client;
import io.crossbar.autobahn.wamp.Session;
import io.crossbar.autobahn.wamp.interfaces.ISession;
import io.crossbar.autobahn.wamp.types.CallResult;
import io.crossbar.autobahn.wamp.types.ExitInfo;
import io.crossbar.autobahn.wamp.types.SessionDetails;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class JavaClient {
  public static void main(String[] arr) throws ExecutionException, InterruptedException {
    Executor executor = Executors.newSingleThreadExecutor();
    Session session = new Session(executor);
    session.addOnJoinListener(JavaClient::callExample);
    Client client = new Client(session, "ws://127.0.0.1:9999","default",executor);
    try {
      client.connect().get();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void callExample(Session session, SessionDetails details) {
    int arg1 = 11, arg2 = 22;
    CompletableFuture<CallResult> completableFuture = session.call("de.danielr1996.add2", arg1, arg2);
    completableFuture.thenAccept(callResult -> System.out.println("Result: " + callResult.results));
  }
}
