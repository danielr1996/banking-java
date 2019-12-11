package de.danielr1996.banking.infrastructure.graphql.datafetchers;

import de.danielr1996.banking.application.auth.AuthenticationService;
import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.infrastructure.graphql.config.GraphQLContext;
import de.danielr1996.banking.infrastructure.tasks.ImportTask;
import graphql.GraphQLException;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RefreshDataFetcher {
  //FIXME: Sollte in ordentlichen DomainService ausgelagert werden
  @Autowired
  @Deprecated
  ImportTask importTask;

  @Autowired
  AuthenticationService authenticationService;

  public DataFetcher<String> refresh() {
    return dataFetchingEnvironment -> {
      GraphQLContext context = dataFetchingEnvironment.getContext();
      String user = authenticationService.isAuthenticated(context.getJwt()).getSubject();

      String username = dataFetchingEnvironment.getArgument("username");
      // FIXME: Authorization
      if (!username.equals(user)) {
        throw new GraphQLException("Not Authorized");
      }
      // FIXME: Remove rpcId
      String rpcId = dataFetchingEnvironment.getArgument("rpcId");
      importTask.importIntoDb(username, rpcId);
      return "Refreshed";
    };
  }
}
