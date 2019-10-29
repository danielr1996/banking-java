package de.danielr1996.banking.infrastructure.graphql.datafetchers;

import java.util.List;
import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.domain.repository.KontoRepository;
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

  public DataFetcher<List<Konto>> getKontoDataFetcher() {
    return dataFetchingEnvironment -> {
      String userId = dataFetchingEnvironment.getArgument("userId");
      if(userId == null){
        throw new GraphQLException("Parameter userId must not be null");
      }

      return kontoRepository.findAll(Example.of(Konto.builder().userId(userId).build()));
    };
  }
}
