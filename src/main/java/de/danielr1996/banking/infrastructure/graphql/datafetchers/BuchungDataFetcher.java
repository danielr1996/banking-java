package de.danielr1996.banking.infrastructure.graphql.datafetchers;

import java.util.Optional;
import java.util.UUID;
import de.danielr1996.banking.application.BuchungService;
import de.danielr1996.banking.application.PageBuchungService;
import de.danielr1996.banking.auth.OwnershipService;
import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.application.BuchungContainer;
import de.danielr1996.banking.infrastructure.graphql.GraphQLContext;
import graphql.GraphQLException;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BuchungDataFetcher {

  @Autowired
  PageBuchungService pageBuchungService;

  @Autowired
  BuchungService buchungService;

  @Autowired
  OwnershipService ownershipService;

  public DataFetcher<Optional<Buchung>> getBuchungByIdDataFetcher() {
    return dataFetchingEnvironment -> {
      String buchungId = dataFetchingEnvironment.getArgument("id");
      String jwt = dataFetchingEnvironment.<GraphQLContext>getContext().getJwt();
      Buchung buchung = buchungService.findById(buchungId);

      if (ownershipService.isOwner(UUID.fromString(jwt), buchung)) {
        return Optional.of(buchung);
      } else {
        throw new GraphQLException("Not Authorized");
      }
    };
  }

  public DataFetcher<BuchungContainer> getBuchungDataFetcher() {
    return dataFetchingEnvironment -> {
      Integer page = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("page")).orElse(0);
      Integer size = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("size")).orElse(10);
      UUID kontoId = UUID.fromString(dataFetchingEnvironment.getArgument("kontoId"));
      return pageBuchungService.getBuchungContainer(kontoId, page, size);
    };
  }
}
