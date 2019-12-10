package de.danielr1996.banking.application.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Base64;

public abstract class AbstractAuthorizationTest {
  @Autowired
  WebTestClient webTestClient;

  public String getJwt(String user, String password) {
    String base64Password = new String(Base64.getEncoder().encode(password.getBytes()));
    String response = webTestClient
      .post()
      .uri("/graphql")
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue("{\n" +
        "\"operationName\": null, \n" +
        "\"query\": \"" + "{signIn(user: {name: \\\"" + user + "\\\", password: \\\"" + base64Password + "\\\"})}" + "\",\n" +
        "\"variables\": {}\n" +
        "}")
      .exchange()
      .expectBody(String.class)
      .returnResult()
      .getResponseBody();
    return response.substring(19, response.length() - 3);
  }
}
