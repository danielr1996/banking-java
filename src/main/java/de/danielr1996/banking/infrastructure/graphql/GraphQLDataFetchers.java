package de.danielr1996.banking.infrastructure.graphql;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.danielr1996.banking.application.GetNewestSaldoService;
import de.danielr1996.banking.application.PageBuchungService;
import de.danielr1996.banking.application.PageSaldoService;
import de.danielr1996.banking.auth.OwnershipService;
import de.danielr1996.banking.auth.User;
import de.danielr1996.banking.auth.UserInput;
import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.domain.repository.BuchungRepository;
import de.danielr1996.banking.domain.repository.KontoRepository;
import de.danielr1996.banking.domain.repository.UserRepository;
import de.danielr1996.banking.infrastructure.tasks.ImportTask;
import graphql.GraphQLException;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GraphQLDataFetchers {
  @Autowired
  private BuchungRepository buchungRepository;

  @Autowired
  PageSaldoService saldoService;

  @Autowired
  GetNewestSaldoService getNewestSaldoService;

  @Autowired
  PageBuchungService pageBuchungService;

  @Autowired
  UserRepository userRepository;

  @Autowired
  OwnershipService ownershipService;

  @Autowired
  KontoRepository kontoRepository;

  //FIXME: Sollte in ordentlichen DomainService ausgelagert werden
  @Autowired
  @Deprecated
  ImportTask importTask;

  DataFetcher<Optional<Buchung>> getBuchungByIdDataFetcher() {
    return dataFetchingEnvironment -> {
      String buchungId = dataFetchingEnvironment.getArgument("id");
      String jwt = dataFetchingEnvironment.<GraphQLContext>getContext().getJwt();
      Buchung buchung = buchungRepository.findById(buchungId).get();

      if (ownershipService.isOwner(UUID.fromString(jwt), buchung)) {
        return Optional.of(buchung);
      } else {
        throw new GraphQLException("Not Authorized");
      }
    };
  }

  DataFetcher<BuchungContainer> getBuchungDataFetcher() {
    return dataFetchingEnvironment -> {
      Integer page = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("page")).orElse(0);
      Integer size = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("size")).orElse(10);
      UUID kontoId = UUID.fromString(dataFetchingEnvironment.getArgument("kontoId"));
      return pageBuchungService.getBuchungContainer(kontoId, page, size);
    };
  }

  DataFetcher<Saldo> getSaldoDataFetcher() {
    return dataFetchingEnvironment -> {
      UUID kontoId = UUID.fromString(dataFetchingEnvironment.getArgument("kontoId"));
      return getNewestSaldoService.getNewestSaldo(kontoId);
    };
  }

  DataFetcher<SaldiContainer> getSaldiDataFetcher() {
    return dataFetchingEnvironment -> {
      log.info("Context: {}", dataFetchingEnvironment.getContext().toString());
      Integer page = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("page")).orElse(0);
      Integer size = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("size")).orElse(10);
      UUID kontoId = UUID.fromString(dataFetchingEnvironment.getArgument("kontoId"));

      return saldoService.getSaldiContainer(kontoId, page, size);
    };
  }

  DataFetcher<User> createUser() {
    return dataFetchingEnvironment -> {
      HashMap<Object, Object> userMap = dataFetchingEnvironment.getArgument("user");
      ObjectMapper mapper = new ObjectMapper();
      UserInput userInput = mapper.convertValue(userMap, UserInput.class);

      User user = User.builder()
        .name(userInput.getName())
        .password(userInput.getPassword())
        .build();

      if(userRepository.existsById(userInput.getName())){
        log.warn("User {} already exists, doing nothing!", user.getName());
      }else{
        userRepository.save(user);
      }

      return user;
    };
  }

  DataFetcher<String> signin() {
    return dataFetchingEnvironment -> {
      HashMap<Object, Object> userMap = dataFetchingEnvironment.getArgument("user");
      ObjectMapper mapper = new ObjectMapper();
      UserInput userInput = mapper.convertValue(userMap, UserInput.class);

      User user = userRepository.findOne(Example.of(User.builder().name(userInput.getName()).build())).get();
      if (user.getPassword().equals(userInput.getPassword())) {
        return user.getName();
      } else {
        throw new GraphQLException("Invalid credentials");
      }
    };
  }

  DataFetcher<String> refresh() {
    return dataFetchingEnvironment -> {
      importTask.importIntoDb(() -> new Scanner(System.in).next(), () -> new Scanner(System.in).next());
      return null;
    };
  }

  DataFetcher<List<Konto>> getKontoDataFetcher() {
    return dataFetchingEnvironment -> {
      String userId = dataFetchingEnvironment.getArgument("userId");
      return kontoRepository.findAll(Example.of(Konto.builder().userId(userId).build()));
    };
  }
}
