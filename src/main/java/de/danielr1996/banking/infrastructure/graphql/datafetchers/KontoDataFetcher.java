package de.danielr1996.banking.infrastructure.graphql.datafetchers;

import java.util.List;
import de.danielr1996.banking.application.auth.AuthenticationService;
import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.domain.repository.KontoRepository;
import de.danielr1996.banking.infrastructure.graphql.GraphQLContext;
import graphql.GraphQLException;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KontoDataFetcher {
  @Autowired
  KontoRepository kontoRepository;

  @Autowired
  AuthenticationService authenticationService;

  public DataFetcher<List<Konto>> getKontoDataFetcher() {
    return dataFetchingEnvironment -> {
      GraphQLContext context = dataFetchingEnvironment.getContext();
      authenticationService.isAuthenticated(context.getJwt());

      String userId;
      if (!dataFetchingEnvironment.containsArgument("userId")) {
        throw new GraphQLException("Parameter userId must not be null");
      }

      userId = dataFetchingEnvironment.getArgument("userId");

      return kontoRepository.findAll(Example.of(Konto.builder().userId(userId).build()));
    };
  }
}
