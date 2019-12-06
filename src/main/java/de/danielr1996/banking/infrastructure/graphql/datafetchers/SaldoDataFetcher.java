package de.danielr1996.banking.infrastructure.graphql.datafetchers;

import java.util.Optional;
import java.util.UUID;
import de.danielr1996.banking.application.auth.AuthenticationService;
import de.danielr1996.banking.application.saldo.dto.SaldiContainer;
import de.danielr1996.banking.application.saldo.service.SaldoApplicationService;
import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.infrastructure.graphql.GraphQLContext;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SaldoDataFetcher {

  @Autowired
  SaldoApplicationService saldoApplicationService;

  @Autowired
  AuthenticationService authenticationService;

  public DataFetcher<Saldo> getSaldoDataFetcher() {
    return dataFetchingEnvironment -> {
      GraphQLContext context = dataFetchingEnvironment.getContext();
      authenticationService.isAuthenticated(context.getJwt());

      UUID kontoId = UUID.fromString(dataFetchingEnvironment.getArgument("kontoId"));
      return saldoApplicationService.getNewestSaldo(kontoId);
    };
  }

  public DataFetcher<SaldiContainer> getSaldiDataFetcher() {
    return dataFetchingEnvironment -> {
      GraphQLContext context = dataFetchingEnvironment.getContext();
      authenticationService.isAuthenticated(context.getJwt());

      Integer page = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("page")).orElse(0);
      Integer size = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("size")).orElse(10);
      UUID kontoId = UUID.fromString(dataFetchingEnvironment.getArgument("kontoId"));

      return saldoApplicationService.getSaldiContainer(kontoId, page, size);
    };
  }
}
