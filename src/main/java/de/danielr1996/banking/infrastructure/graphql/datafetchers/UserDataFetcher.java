package de.danielr1996.banking.infrastructure.graphql.datafetchers;

import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.danielr1996.banking.application.auth.TokenGenerator;
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

  public DataFetcher<User> createUser() {
    return dataFetchingEnvironment -> {
      HashMap<Object, Object> userMap = dataFetchingEnvironment.getArgument("user");
      ObjectMapper mapper = new ObjectMapper();
      UserInput userInput = mapper.convertValue(userMap, UserInput.class);

      User user = User.builder()
        .name(userInput.getName())
        .passwordhash(userInput.getPasswordhash())
        .build();

      if(userRepository.existsById(userInput.getName())){
        log.warn("User {} already exists, doing nothing!", user.getName());
      }else{
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

      User user = userRepository.findOne(Example.of(User.builder().name(userInput.getName()).build())).orElseThrow(()->new GraphQLException("Not Found"));
      if (user.getPasswordhash().equals(userInput.getPasswordhash())) {
        return jwtHelper.generate(user);
      } else {
        throw new GraphQLException("Invalid credentials");
      }
    };
  }
}
