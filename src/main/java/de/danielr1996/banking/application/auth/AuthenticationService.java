package de.danielr1996.banking.application.auth;

import java.util.Optional;
import graphql.GraphQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService {
  @Autowired
  TokenVerifier.TokenVerifierFactory tokenVerifierFactory;

  public void isAuthenticated(Optional<String> jwt) {
    String jwtString = jwt.orElseThrow(() -> new GraphQLException("Not Authenticated, JWT Empty"));
    log.info("Context: {}", jwt);
    try {
      tokenVerifierFactory.ofJwt(jwtString).verify();
    } catch (Exception e) {
      throw new GraphQLException("Not Authenticated, JWT Wrong");
    }
  }
}
