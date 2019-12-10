package de.danielr1996.banking.infrastructure.graphql.datafetchers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import de.danielr1996.banking.application.auth.AuthenticationService;
import de.danielr1996.banking.application.saldo.dto.SaldiContainer;
import de.danielr1996.banking.application.saldo.service.SaldoApplicationService;
import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.domain.repository.KontoRepository;
import de.danielr1996.banking.infrastructure.graphql.config.GraphQLContext;
import graphql.GraphQLException;
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

  @Autowired
  KontoRepository kontoRepository;

  public DataFetcher<Saldo> getSaldoDataFetcher() {
    return dataFetchingEnvironment -> {
      GraphQLContext context = dataFetchingEnvironment.getContext();
      String user = authenticationService.isAuthenticated(context.getJwt()).getSubject();
      List<String> kontoIdStrings = dataFetchingEnvironment.getArgument("kontoIds");
      List<UUID> kontoIds = kontoIdStrings.stream().map(UUID::fromString).collect(Collectors.toList());

      // FIXME: Authorization
      boolean onlyOwnedKontos = kontoIds.stream().allMatch(kontoId -> {
        Konto konto = kontoRepository.findById(kontoId).orElseThrow(() -> new GraphQLException("Not Found"));
        return konto.getUserId().equals(user);
      });

      if (!onlyOwnedKontos) {
        throw new GraphQLException("Not Authorized");
      }

      return saldoApplicationService.getSaldo(kontoIds);
    };
  }

  public DataFetcher<SaldiContainer> getSaldiDataFetcher() {
    return dataFetchingEnvironment -> {
      GraphQLContext context = dataFetchingEnvironment.getContext();
      String user = authenticationService.isAuthenticated(context.getJwt()).getSubject();

      Integer page = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("page")).orElse(0);
      Integer size = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("size")).orElse(10);
      List<String> kontoIdStrings = dataFetchingEnvironment.getArgument("kontoIds");
      List<UUID> kontoIds = kontoIdStrings.stream().map(UUID::fromString).collect(Collectors.toList());
      // FIXME: Authorization
      boolean onlyOwnedKontos = kontoIds.stream().allMatch(kontoId -> {
        Konto konto = kontoRepository.findById(kontoId).orElseThrow(() -> new GraphQLException("Not Found"));
        return konto.getUserId().equals(user);
      });

      if (!onlyOwnedKontos) {
        throw new GraphQLException("Not Authorized");
      }


      return saldoApplicationService.getSaldiContainer(kontoIds, page, size);
    };
  }
}
