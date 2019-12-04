package de.danielr1996.banking;

import io.crossbar.autobahn.wamp.Client;
import io.crossbar.autobahn.wamp.Session;
import io.crossbar.autobahn.wamp.interfaces.ISession;
import io.crossbar.autobahn.wamp.types.CallResult;
import io.crossbar.autobahn.wamp.types.ExitInfo;
import io.crossbar.autobahn.wamp.types.SessionDetails;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class JavaClient {
  public static void main(String[] arr) throws ExecutionException, InterruptedException, TimeoutException {
    Session session = new Session();
    CountDownLatch latch = new CountDownLatch(1);
    session.addOnJoinListener((Session s, SessionDetails sd)->latch.countDown());
    Client client = new Client(session, "ws://127.0.0.1:9090/wamp", "default");
    CompletableFuture<ExitInfo> connection = client.connect();

    latch.await();
    List<Object> result = session.call("de.danielr1996.add2", 11, 12).get().results;
  }

  private static void callExample(Session session, SessionDetails details) {
    try {
      List<Object> result = session.call("de.danielr1996.add2", 11, 12).get().results;
      System.out.println(result.get(0));
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }

  }
}
