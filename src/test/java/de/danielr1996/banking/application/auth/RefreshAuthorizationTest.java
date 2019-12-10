package de.danielr1996.banking.application.auth;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

// FIXME: Use GraphQL Library instead of Plain HTTP
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@ActiveProfiles("db, db-h2, fints-mock, hbcicallback-static")
@Tags({
  @Tag("graphql"),
  @Tag("application"),
  @Tag("authorization")
})
class RefreshAuthorizationTest extends AbstractAuthorizationTest {
  @Test
  void testUser1withCorrectJwtCanRefresh() {
    final String JWT = getJwt("user1", "password1");
    System.out.println("JWT: " + JWT);
    String response = webTestClient
      .post()
      .uri("/graphql")
      .header("Authorization", "Bearer " + JWT)
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue("{\n" +
        "\"operationName\": null, \n" +
        "\"query\": \"" + "{refresh(rpcId: \\\"201910280705\\\", username: \\\"user1\\\")}" + "\",\n" +
        "\"variables\": {}\n" +
        "}")
      .exchange()
      .expectBody(String.class)
      .returnResult()
      .getResponseBody();
    System.out.println(response);
    assertThat(response, Matchers.allOf(
      not(containsString("error")),
      not(containsString("Not Authenticated, JWT Wrong")),
      not(containsString("Not Authenticated, JWT Empty")),
      not(containsString("Not Authorized")),
      not(containsString("Not Found"))
    ));
  }

  @Test
  void testUser2withWrongJwtCantRefresh() {
    final String JWT = getJwt("user2", "password2");
    System.out.println(JWT);
    String response = webTestClient
      .post()
      .uri("/graphql")
      .header("Authorization", "Bearer " + JWT)
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue("{\n" +
        "\"operationName\": null, \n" +
        "\"query\": \"" + "{refresh(rpcId: \\\"201910280705\\\", username: \\\"user1\\\")}" + "\",\n" +
        "\"variables\": {}\n" +
        "}")
      .exchange()
      .expectBody(String.class)
      .returnResult()
      .getResponseBody();

    assertThat(response, Matchers.allOf(
      containsString("error"),
      containsString("Not Authorized")
    ));
  }
}
