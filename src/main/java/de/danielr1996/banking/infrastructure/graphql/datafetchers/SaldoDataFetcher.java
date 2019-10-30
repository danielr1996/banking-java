package de.danielr1996.banking.infrastructure.graphql.datafetchers;

import java.util.Optional;
import java.util.UUID;
import de.danielr1996.banking.application.GetNewestSaldoService;
import de.danielr1996.banking.application.PageSaldoService;
import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.application.SaldiContainer;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SaldoDataFetcher {
  @Autowired
  PageSaldoService saldoService;

  @Autowired
  GetNewestSaldoService getNewestSaldoService;

  public DataFetcher<Saldo> getSaldoDataFetcher() {
    return dataFetchingEnvironment -> {
      UUID kontoId = UUID.fromString(dataFetchingEnvironment.getArgument("kontoId"));
      return getNewestSaldoService.getNewestSaldo(kontoId);
    };
  }

  public DataFetcher<SaldiContainer> getSaldiDataFetcher() {
    return dataFetchingEnvironment -> {
      log.info("Context: {}", dataFetchingEnvironment.getContext().toString());
      Integer page = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("page")).orElse(0);
      Integer size = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("size")).orElse(10);
      UUID kontoId = UUID.fromString(dataFetchingEnvironment.getArgument("kontoId"));

      return saldoService.getSaldiContainer(kontoId, page, size);
    };
  }
}
