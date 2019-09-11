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
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@Primary
public class ContextGraphQLInvocation  implements GraphQLInvocation {

  @Autowired
  GraphQL graphQL;

  @Autowired(required = false)
  DataLoaderRegistry dataLoaderRegistry;

//  @Autowired
//  ExecutionInputCustomizer executionInputCustomizer;

  @Override
  public CompletableFuture<ExecutionResult> invoke(GraphQLInvocationData invocationData, WebRequest webRequest) {
    log.info("Invoked ContextGraphQLInvocation");
    ExecutionInput.Builder executionInputBuilder = ExecutionInput.newExecutionInput()
      .query(invocationData.getQuery())
      .context("Userid: abc88")
      .operationName(invocationData.getOperationName())
      .variables(invocationData.getVariables());
    if (dataLoaderRegistry != null) {
      executionInputBuilder.dataLoaderRegistry(dataLoaderRegistry);
    }
    ExecutionInput executionInput = executionInputBuilder.build();
    CompletableFuture<ExecutionInput> customizedExecutionInput = CompletableFuture.completedFuture(executionInput);
    return customizedExecutionInput.thenCompose(graphQL::executeAsync);
  }

}
