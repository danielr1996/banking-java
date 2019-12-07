package de.danielr1996.banking.infrastructure.graphql.datafetchers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import de.danielr1996.banking.application.auth.AuthenticationService;
import de.danielr1996.banking.application.buchung.dto.BuchungDTO;
import de.danielr1996.banking.application.buchung.service.BuchungApplicationService;
import de.danielr1996.banking.application.buchung.dto.KontoDTO;
import de.danielr1996.banking.application.auth.OwnershipService;
import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.application.buchung.dto.BuchungContainer;
import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.domain.repository.KontoRepository;
import de.danielr1996.banking.infrastructure.graphql.GraphQLContext;
import graphql.GraphQLException;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BuchungDataFetcher {
  @Autowired
  BuchungApplicationService buchungApplicationService;

  @Autowired
  OwnershipService ownershipService;

  @Autowired
  KontoRepository kontoRepository;

  @Autowired
  AuthenticationService authenticationService;

  public DataFetcher<Optional<Buchung>> getBuchungByIdDataFetcher() {
    return dataFetchingEnvironment -> {
      GraphQLContext context = dataFetchingEnvironment.getContext();
      authenticationService.isAuthenticated(context.getJwt());

      String buchungId = dataFetchingEnvironment.getArgument("id");
      Buchung buchung = buchungApplicationService.findById(buchungId).orElseThrow(() -> new GraphQLException("Not Found"));

      /*if (ownershipService.isOwner(UUID.fromString(jwt), buchung)) {*/
      return Optional.of(buchung);
      /*} else {
        throw new GraphQLException("Not Authenticated, JWT Empty");
      }*/
    };
  }

  public DataFetcher<BuchungContainer> getBuchungDataFetcher() {
    return dataFetchingEnvironment -> {
      GraphQLContext context = dataFetchingEnvironment.getContext();
      authenticationService.isAuthenticated(context.getJwt());

      int page = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("page")).orElse(0);
      int size = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("size")).orElse(10);
      List<String> kontoIdStrings = dataFetchingEnvironment.getArgument("kontoIds");
      List<UUID> kontoIds = kontoIdStrings.stream().map(UUID::fromString).collect(Collectors.toList());

      if (!dataFetchingEnvironment.getSelectionSet().contains("buchungen/konto")) {
        return buchungApplicationService.getBuchungContainer(kontoIds, page, size);
      } else {
        BuchungContainer buchungContainer = buchungApplicationService.getBuchungContainer(kontoIds, page, size);
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
