package de.danielr1996.banking.infrastructure.graphql;

import de.danielr1996.banking.repository.BuchungRepository;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class GraphQLDataFetchers {
  @Autowired
  private BuchungRepository buchungRepository;

  public DataFetcher getBuchungByIdDataFetcher() {
    return dataFetchingEnvironment -> {
      String buchungId = dataFetchingEnvironment.getArgument("id");

      return buchungRepository.findById(UUID.fromString(buchungId));
    };
  }

  public DataFetcher getBuchungDataFetcher() {
    return dataFetchingEnvironment -> {
      Optional<Integer> page = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("page"));
      Optional<Integer> size = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("size"));
      return buchungRepository.findAll(PageRequest.of(page.orElse(0), size.orElse(10), Sort.by(Sort.Order.desc("id"))));
    };
  }
}
