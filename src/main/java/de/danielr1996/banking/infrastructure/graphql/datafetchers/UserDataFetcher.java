package de.danielr1996.banking.infrastructure.graphql.datafetchers;

import java.util.Base64;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.danielr1996.banking.application.auth.Password;
import de.danielr1996.banking.infrastructure.security.PasswordHasher;
import de.danielr1996.banking.infrastructure.security.TokenGenerator;
import de.danielr1996.banking.application.auth.User;
import de.danielr1996.banking.application.auth.UserInput;
import de.danielr1996.banking.domain.repository.UserRepository;
import graphql.GraphQLException;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserDataFetcher {
  @Autowired
  UserRepository userRepository;

  @Autowired
  TokenGenerator jwtHelper;

  @Autowired
  PasswordHasher passwordHasher;

  public DataFetcher<User> createUser() {
    return dataFetchingEnvironment -> {
      HashMap<Object, Object> userMap = dataFetchingEnvironment.getArgument("user");
      ObjectMapper mapper = new ObjectMapper();
      UserInput userInput = mapper.convertValue(userMap, UserInput.class);
      String password = new String(Base64.getDecoder().decode(userInput.getPassword()));
      User user = User.builder()
        .name(userInput.getName())
        .password(passwordHasher.hash(password))
        .build();

      if (userRepository.existsById(userInput.getName())) {
        log.warn("User {} already exists, doing nothing!", user.getName());
      } else {
        userRepository.save(user);
      }

      return user;
    };
  }

  public DataFetcher<String> signin() {
    return dataFetchingEnvironment -> {
      HashMap<Object, Object> userMap = dataFetchingEnvironment.getArgument("user");
      ObjectMapper mapper = new ObjectMapper();
      UserInput userInput = mapper.convertValue(userMap, UserInput.class);

      User user = userRepository.findByName(userInput.getName()).orElseThrow(() -> new GraphQLException("Not Found"));
      String password = new String(Base64.getDecoder().decode(userInput.getPassword()));
      if (passwordHasher.verify(password, user.getPassword())) {
        return jwtHelper.generate(user);
      } else {
        throw new GraphQLException("Invalid Credentials");
      }
    };
  }
}
