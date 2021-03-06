package de.danielr1996.banking.infrastructure.graphql.datafetchers;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.danielr1996.banking.application.auth.AuthenticationService;
import de.danielr1996.banking.application.auth.UserInput;
import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.domain.repository.KontoRepository;
import de.danielr1996.banking.infrastructure.graphql.config.GraphQLContext;
import de.danielr1996.banking.infrastructure.security.PasswordEncrypter;
import graphql.GraphQLException;
import graphql.schema.DataFetcher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KontoDataFetcher {
  @Autowired
  KontoRepository kontoRepository;

  @Autowired
  AuthenticationService authenticationService;

  @Autowired
  PasswordEncrypter passwordEncrypter;

  public DataFetcher<List<Konto>> getKontoDataFetcher() {
    return dataFetchingEnvironment -> {
      GraphQLContext context = dataFetchingEnvironment.getContext();
      String actualUser = authenticationService.isAuthenticated(context.getJwt()).getSubject();
      String userId = dataFetchingEnvironment.getArgument("username");

      if (!userId.equals(actualUser)) {
        throw new GraphQLException("Not Authorized");
      }

      return kontoRepository.findAll(Example.of(Konto.builder().userId(userId).build()));
    };
  }

  public DataFetcher<Konto> getCreateKontoDataFetcher() {
    return dataFetchingEnvironment -> {
      GraphQLContext context = dataFetchingEnvironment.getContext();
      String user = authenticationService.isAuthenticated(context.getJwt()).getSubject();

      HashMap<Object, Object> userMap = dataFetchingEnvironment.getArgument("konto");
      ObjectMapper mapper = new ObjectMapper();
      KontoInput kontoInput = mapper.convertValue(userMap, KontoInput.class);

      Konto konto = Konto.builder()
        .userId(user)
        .blz(kontoInput.getBlz())
        .id(kontoInput.getId())
        .kontonummer(kontoInput.getKontonummer())
        .passwordhash(passwordEncrypter.encrypt(kontoInput.getPassword()))
        .secmech(kontoInput.getSecmech())
        .tanmedia(kontoInput.getTanmedia())
        .bankaccount(kontoInput.getBankaccount())
        .build();

      return kontoRepository.save(konto);
    };
  }

  public DataFetcher<Konto> getDeleteKontoDataFetcher() {
    return dataFetchingEnvironment -> {
      GraphQLContext context = dataFetchingEnvironment.getContext();
      String actualUser = authenticationService.isAuthenticated(context.getJwt()).getSubject();
      UUID kontoId = UUID.fromString(dataFetchingEnvironment.getArgument("kontoId"));

      Konto konto = kontoRepository.findById(kontoId).orElseThrow(() -> new GraphQLException("Not Found"));

      if (!konto.getUserId().equals(actualUser)) {
        throw new GraphQLException("Not Authorized");
      }

      kontoRepository.delete(konto);

      return konto;
    };
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class KontoInput {
    private String blz;
    private String kontonummer;
    private String tanmedia;
    private String secmech;
    private String password;
    private String bankaccount;
    private UUID id;
  }
}
