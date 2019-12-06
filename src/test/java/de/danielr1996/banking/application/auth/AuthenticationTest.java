package de.danielr1996.banking.application.auth;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

// FIXME: Use GraphQL Library instead of Plain HTTP
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@ActiveProfiles("db, db-h2, fints-mock, hbcicallback-static")
class AuthenticationTest {

  @Autowired
  WebTestClient webTestClient;


  @ParameterizedTest
  @ValueSource(strings = {
    "{buchungById(id: \\\"201910280705\\\") {id}}",
    "{buchungen(kontoIds: [\\\"42601f3b-6e91-4c80-bb11-c5a21d98fc57\\\"], page: 0, size: 10) {totalElements}}",
    "{konto(userId: \\\"user1\\\") {id}}",
    "{saldi(kontoId: \\\"42601f3b-6e91-4c80-bb11-c5a21d98fc57\\\", page: 0, size: 10) {totalElements}}",
    "{saldo(kontoId: \\\"42601f3b-6e91-4c80-bb11-c5a21d98fc57\\\") {betrag}}",
    "mutation{refresh(rpcId: \\\"201910280705\\\", username: \\\"user1\\\")}",
  })
  void testAuthenticationWithNoJwtShouldBeForbidden(String query) {
    String response = webTestClient
      .post()
      .uri("/graphql")
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue("{\n" +
        "\"operationName\": null, \n" +
        "\"query\": \"" + query + "\",\n" +
        "\"variables\": {}\n" +
        "}")
      .exchange()
      .expectBody(String.class)
      .returnResult()
      .getResponseBody();

    assertThat(response, containsString("Not Authenticated, JWT Empty"));
  }

  @ParameterizedTest
  @ValueSource(strings = {
    "{buchungById(id: \\\"201910280705\\\") {id}}",
    "{buchungen(kontoIds: [\\\"42601f3b-6e91-4c80-bb11-c5a21d98fc57\\\"], page: 0, size: 10) {totalElements}}",
    "{konto(userId: \\\"user1\\\") {id}}",
    "{saldi(kontoId: \\\"42601f3b-6e91-4c80-bb11-c5a21d98fc57\\\", page: 0, size: 10) {totalElements}}",
    "{saldo(kontoId: \\\"42601f3b-6e91-4c80-bb11-c5a21d98fc57\\\") {betrag}}",
    "mutation{refresh(rpcId: \\\"201910280705\\\", username: \\\"user1\\\")}",
  })
  void testAuthenticationWithWrongJwtShouldBeForbidden(String query) {
    final String USER_NOT_EXISTENT_JWT = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2Vybm90ZXhpc3RlbmQifQ.bwnNhzzYvjBeykFjApp8FmFolqmKBy9bUmfvfwan5m0";

    String response = webTestClient
      .post()
      .uri("/graphql")
      .contentType(MediaType.APPLICATION_JSON)
      .header("Authorization", "Bearer " + USER_NOT_EXISTENT_JWT)
      .bodyValue("{\n" +
        "\"operationName\": null, \n" +
        "\"query\": \"" + query + "\",\n" +
        "\"variables\": {}\n" +
        "}")
      .exchange()
      .expectBody(String.class)
      .returnResult()
      .getResponseBody();

    assertThat(response, containsString("Not Authenticated, JWT Wrong"));
  }

  @ParameterizedTest
  @ValueSource(strings = {
    "{buchungById(id: \\\"201910280705\\\") {id}}",
    "{buchungen(kontoIds: [\\\"42601f3b-6e91-4c80-bb11-c5a21d98fc57\\\"], page: 0, size: 10) {totalElements}}",
    "{konto(userId: \\\"user1\\\") {id}}",
    "{saldi(kontoId: \\\"42601f3b-6e91-4c80-bb11-c5a21d98fc57\\\", page: 0, size: 10) {totalElements}}",
    "{saldo(kontoId: \\\"42601f3b-6e91-4c80-bb11-c5a21d98fc57\\\") {betrag}}",
    "mutation{refresh(rpcId: \\\"201910280705\\\", username: \\\"user1\\\")}",
  })
  void testAuthenticationWithJwtShouldBeAllowed(String query) {
    final String JWT = getJwt("user1", "password1");

    String response = webTestClient
      .post()
      .uri("/graphql")
      .contentType(MediaType.APPLICATION_JSON)
      .header("Authorization", "Bearer " + JWT)
      .bodyValue("{\n" +
        "\"operationName\": null, \n" +
        "\"query\": \"" + query + "\",\n" +
        "\"variables\": {}\n" +
        "}")
      .exchange()
      .expectBody(String.class)
      .returnResult()
      .getResponseBody();

    assertThat(response, Matchers.allOf(
      not(containsString("error")),
      not(containsString("Not Authenticated, JWT Wrong")),
      not(containsString("Not Authenticated, JWT Empty"))));
  }

  public String getJwt(String user, String password) {
    String response = webTestClient
      .post()
      .uri("/graphql")
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue("{\n" +
        "\"operationName\": null, \n" +
        "\"query\": \"" + "{signIn(user: {name: \\\"user1\\\", password: \\\"password1\\\"})}" + "\",\n" +
        "\"variables\": {}\n" +
        "}")
      .exchange()
      .expectBody(String.class)
      .returnResult()
      .getResponseBody();
    return response.substring(19, response.length() - 3);
  }
}
