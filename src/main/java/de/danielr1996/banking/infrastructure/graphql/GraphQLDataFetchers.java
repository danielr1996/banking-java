package de.danielr1996.banking.infrastructure.graphql;

import de.danielr1996.banking.domain.Buchung;
import de.danielr1996.banking.repository.BuchungRepository;
import de.danielr1996.banking.repository.SaldoRepository;
import de.danielr1996.banking.services.SaldoService;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
      Optional<Integer> page = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("page"));
      Optional<Integer> size = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("size"));
      Page<Buchung> buchungen = buchungRepository.findAll(PageRequest.of(page.orElse(0), size.orElse(10), Sort.by(Sort.Order.desc("id"))));

      return BuchungContainer.builder()
        .buchungen(buchungen.getContent())
        .totalElements(buchungen.getTotalElements())
        .totalPages(buchungen.getTotalPages())
        .build();
    };
  }

  public DataFetcher getSaldoDataFetcher(){
    return dataFetchingEnvironment -> {
      return saldoRepository.findAll().get(0);
    };
  }
  public DataFetcher getSaldiDataFetcher(){
    return dataFetchingEnvironment -> {
      return SaldoService.getSaldi(buchungRepository.findAll(), saldoRepository.findAll().get(0));
    };
  }
}
