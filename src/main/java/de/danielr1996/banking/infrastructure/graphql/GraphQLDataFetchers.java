package de.danielr1996.banking.infrastructure.graphql;

import de.danielr1996.banking.domain.Buchung;
import de.danielr1996.banking.domain.Saldo;
import de.danielr1996.banking.repository.BuchungRepository;
import de.danielr1996.banking.repository.SaldoRepository;
import de.danielr1996.banking.services.SaldoService;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class GraphQLDataFetchers {
  @Autowired
  private BuchungRepository buchungRepository;

  @Autowired
  private SaldoRepository saldoRepository;

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
      Page<Buchung> buchungen = buchungRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Order.desc("id"))));

      return BuchungContainer.builder()
        .buchungen(buchungen.getContent())
        .totalElements(buchungen.getTotalElements())
        .totalPages(buchungen.getTotalPages())
        .build();
    };
  }

  public DataFetcher getSaldoDataFetcher() {
    return dataFetchingEnvironment -> {
      return saldoRepository.findAll().get(0);
    };
  }

  public DataFetcher getSaldiDataFetcher() {
    return dataFetchingEnvironment -> {
      Integer page = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("page")).orElse(0);
      Integer size = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("size")).orElse(10);
      // FIXME: Kein Element vorhanden
      List<Saldo> saldi = SaldoService.getSaldi(
        buchungRepository.findAll(),
        saldoRepository
          .findAll()
          .stream()
          .min(Comparator.comparing(Saldo::getDatum))
          .get())
        .stream()
        .skip(page * size)
        .limit(size)
        .collect(Collectors.toList());
      return SaldiContainer.builder()
        .saldi(saldi)
        .totalElements(saldi.size())
        .build();
    };
  }
}
