package de.danielr1996.banking.infrastructure.graphql;

import de.danielr1996.banking.application.GetNewestSaldoService;
import de.danielr1996.banking.application.PageBuchungService;
import de.danielr1996.banking.application.PageSaldoService;
import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.repository.BuchungRepository;
import de.danielr1996.banking.repository.SaldoRepository;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GraphQLDataFetchers {
  @Autowired
  private BuchungRepository buchungRepository;

  @Autowired
  private SaldoRepository saldoRepository;

  @Autowired
  PageSaldoService saldoService;

  @Autowired
  GetNewestSaldoService getNewestSaldoService;

  @Autowired
  PageBuchungService pageBuchungService;

  public DataFetcher getBuchungByIdDataFetcher() {
    return dataFetchingEnvironment -> {
      String buchungId = dataFetchingEnvironment.getArgument("id");

      return buchungRepository.findById(buchungId);
    };
  }

  public DataFetcher getBuchungDataFetcher() {
    return dataFetchingEnvironment -> {
      Integer page = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("page")).orElse(0);
      Integer size = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("size")).orElse(10);
      return pageBuchungService.getBuchungContainer(page, size);
    };
  }

  public DataFetcher getSaldoDataFetcher() {
    return dataFetchingEnvironment -> getNewestSaldoService.getNewestSaldo();
  }

  public DataFetcher getSaldiDataFetcher() {
    return dataFetchingEnvironment -> {
      Integer page = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("page")).orElse(0);
      Integer size = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("size")).orElse(10);

      return saldoService.getSaldiContainer(page, size);
    };
  }
}
