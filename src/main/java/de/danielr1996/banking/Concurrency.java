package de.danielr1996.banking;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Concurrency {
  public static void main(String[] args) {
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    Future<String> future = executorService.submit(() -> {
      Thread.sleep(2000);
      return "Hallo";
    });

    try {
      String result = future.get();
      System.out.println(result);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
  }
}
