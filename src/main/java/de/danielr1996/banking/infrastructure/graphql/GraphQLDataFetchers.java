package de.danielr1996.banking.infrastructure.graphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.danielr1996.banking.application.GetNewestSaldoService;
import de.danielr1996.banking.application.PageBuchungService;
import de.danielr1996.banking.application.PageSaldoService;
import de.danielr1996.banking.auth.OwnershipService;
import de.danielr1996.banking.auth.User;
import de.danielr1996.banking.auth.UserInput;
import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.domain.repository.BuchungRepository;
import de.danielr1996.banking.domain.repository.UserRepository;
import graphql.GraphQLException;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

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

  DataFetcher<Optional<Buchung>> getBuchungByIdDataFetcher() {
    return dataFetchingEnvironment -> {
      String buchungId = dataFetchingEnvironment.getArgument("id");
      String jwt = dataFetchingEnvironment.<GraphQLContext>getContext().getJwt();
      Buchung buchung = buchungRepository.findById(buchungId).get();

      if(ownershipService.isOwner(UUID.fromString(jwt), buchung)){
        return Optional.of(buchung);
      }else{
        throw new GraphQLException("Not Authorized");
      }
    };
  }

  DataFetcher<BuchungContainer> getBuchungDataFetcher() {
    return dataFetchingEnvironment -> {
      Integer page = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("page")).orElse(0);
      Integer size = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("size")).orElse(10);
      return pageBuchungService.getBuchungContainer(page, size);
    };
  }

  DataFetcher<Saldo> getSaldoDataFetcher() {
    return dataFetchingEnvironment -> getNewestSaldoService.getNewestSaldo();
  }

  DataFetcher<SaldiContainer> getSaldiDataFetcher() {
    return dataFetchingEnvironment -> {
      log.info("Context: {}", dataFetchingEnvironment.getContext().toString());
      Integer page = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("page")).orElse(0);
      Integer size = Optional.ofNullable(dataFetchingEnvironment.<Integer>getArgument("size")).orElse(10);

      return saldoService.getSaldiContainer(page, size);
    };
  }

  DataFetcher<User> createUser() {
    return dataFetchingEnvironment -> {
      HashMap<Object, Object> userMap = dataFetchingEnvironment.getArgument("user");
      ObjectMapper mapper = new ObjectMapper();
      UserInput userInput = mapper.convertValue(userMap, UserInput.class);

      User user = User.builder()
        .id(UUID.randomUUID())
        .name(userInput.getName())
        .password(userInput.getPassword())
        .build();

      userRepository.save(user);

      return user;
    };
  }

  DataFetcher<String> signin() {
    return dataFetchingEnvironment -> {
      HashMap<Object, Object> userMap = dataFetchingEnvironment.getArgument("user");
      ObjectMapper mapper = new ObjectMapper();
      UserInput userInput = mapper.convertValue(userMap, UserInput.class);

      User user = userRepository.findOne(Example.of(User.builder().name(userInput.getName()).build())).get();
      log.info("{}", user);
      if (user.getPassword().equals(userInput.getPassword())) {
        return user.getId().toString();
      } else {
        throw new GraphQLException("Invalid credentials");
      }
    };
  }
}
