package de.danielr1996.banking.infrastructure.graphql.datafetchers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import de.danielr1996.banking.application.buchung.BuchungDTO;
import de.danielr1996.banking.application.buchung.BuchungService;
import de.danielr1996.banking.application.buchung.KontoDTO;
import de.danielr1996.banking.application.buchung.PageBuchungService;
import de.danielr1996.banking.application.auth.OwnershipService;
import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.application.buchung.BuchungContainer;
import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.domain.repository.KontoRepository;
import de.danielr1996.banking.infrastructure.graphql.GraphQLContext;
import graphql.GraphQLException;
import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingFieldSelectionSet;
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

  @Autowired
  KontoRepository kontoRepository;

  public DataFetcher<Optional<Buchung>> getBuchungByIdDataFetcher() {
    return dataFetchingEnvironment -> {
      String buchungId = dataFetchingEnvironment.getArgument("id");
      Buchung buchung = buchungService.findById(buchungId).orElseThrow(() -> new GraphQLException("Not Found"));
      String jwt = dataFetchingEnvironment.<GraphQLContext>getContext().getJwt();

      if ("".equals(jwt)) {
        throw new GraphQLException("Not Authorized");
      } else if (ownershipService.isOwner(UUID.fromString(jwt), buchung)) {
        return Optional.of(buchung);
      } else {
        throw new GraphQLException("Not Authorized");
      }

    };
  }

  public DataFetcher<BuchungContainer> getBuchungDataFetcher() {
    return dataFetchingEnvironment -> {
      String jwt = dataFetchingEnvironment.<GraphQLContext>getContext().getJwt();

      if ("".equals(jwt)) {
        throw new GraphQLException("Not Authorized");
      }

      int page = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("page")).orElse(0);
      int size = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("size")).orElse(10);
      List<String> kontoIdStrings = dataFetchingEnvironment.getArgument("kontoIds");
      List<UUID> kontoIds = kontoIdStrings.stream().map(UUID::fromString).collect(Collectors.toList());

      if (!dataFetchingEnvironment.getSelectionSet().contains("buchungen/konto")) {
        return pageBuchungService.getBuchungContainer(kontoIds, page, size);
      } else {
        BuchungContainer buchungContainer = pageBuchungService.getBuchungContainer(kontoIds, page, size);
        List<BuchungDTO> buchungList = buchungContainer.getBuchungen().stream().map(buchung -> {
          Konto konto = kontoRepository.getOne(buchung.getKontoId());
          buchung.setKonto(KontoDTO.builder()
            .id(konto.getId())
            .blz(konto.getBlz())
            .kontonummer(konto.getKontonummer())
            .build());
          return buchung;
        }).collect(Collectors.toList());
        buchungContainer.setBuchungen(buchungList);
        return buchungContainer;
      }
    };
  }
}
