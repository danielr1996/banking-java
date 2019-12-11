package de.danielr1996.banking.application.auth;

import java.util.Optional;

import de.danielr1996.banking.infrastructure.security.TokenVerifier;
import graphql.GraphQLException;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService {
  @Autowired
  TokenVerifier.TokenVerifierFactory tokenVerifierFactory;

  public Claims isAuthenticated(Optional<String> jwt) {
    String jwtString = jwt.orElseThrow(() -> new GraphQLException("Not Authenticated, JWT Empty"));
    try {
      return tokenVerifierFactory.ofJwt(jwtString).verify();
    } catch (Exception e) {
      log.info("Cannot verify token because {}", e.getMessage());
      throw new GraphQLException("Not Authenticated, JWT Wrong");
    }
  }
}
