package de.danielr1996.banking.infrastructure.graphql.datafetchers;

import java.util.Scanner;
import de.danielr1996.banking.infrastructure.tasks.ImportTask;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RefreshDataFetcher {
  //FIXME: Sollte in ordentlichen DomainService ausgelagert werden
  @Autowired
  @Deprecated
  ImportTask importTask;

  public DataFetcher<String> refresh() {
    return dataFetchingEnvironment -> {
      String username = dataFetchingEnvironment.getArgument("username");

      // FIXME: Remove rpcId
      String rpcId = dataFetchingEnvironment.getArgument("rpcId");
      importTask.importIntoDb(() -> new Scanner(System.in).next(), () -> new Scanner(System.in).next(),username, rpcId);
      return null;
    };
  }
}
