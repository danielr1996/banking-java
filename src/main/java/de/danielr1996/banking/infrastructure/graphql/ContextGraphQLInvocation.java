package de.danielr1996.banking.infrastructure.graphql;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.spring.web.servlet.GraphQLInvocation;
import graphql.spring.web.servlet.GraphQLInvocationData;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoaderRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@Primary
public class ContextGraphQLInvocation implements GraphQLInvocation {
  private static final String HEADER_AUTHORIZATION = "Authorization";
  @Autowired
  GraphQL graphQL;

  @Autowired(required = false)
  DataLoaderRegistry dataLoaderRegistry;

  @Override
  public CompletableFuture<ExecutionResult> invoke(GraphQLInvocationData invocationData, WebRequest webRequest) {
    Optional<String> auth = Optional.ofNullable(webRequest.getHeader(HEADER_AUTHORIZATION));
    ExecutionInput.Builder executionInputBuilder = ExecutionInput.newExecutionInput()
      .query(invocationData.getQuery())
      .context(GraphQLContext.builder()
        .jwt(auth.map(a -> a.replace("Bearer ", "")))
        .build())
      .operationName(invocationData.getOperationName())
      .variables(invocationData.getVariables());
    if (dataLoaderRegistry != null) {
      executionInputBuilder.dataLoaderRegistry(dataLoaderRegistry);
    }
    ExecutionInput executionInput = executionInputBuilder.build();
    return CompletableFuture.completedFuture(executionInput).thenCompose(graphQL::executeAsync);
  }

}
