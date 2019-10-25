package de.danielr1996.banking.infrastructure.graphql;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;
import java.io.IOException;
import java.net.URL;
import javax.annotation.PostConstruct;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GraphQLProvider {
  private GraphQL graphQL;
  private String TYPE_QUERY = "Query";
  private String TYPE_MUTATION = "Mutation";

  @Autowired
  GraphQLDataFetchers graphQLDataFetchers;

  @Bean
  public GraphQL graphQL() {
    return graphQL;
  }

  @PostConstruct
  public void init() throws IOException {
    URL url = Resources.getResource("schema.graphqls");
    String sdl = Resources.toString(url, Charsets.UTF_8);
    GraphQLSchema schema = buildSchema(sdl);
    this.graphQL = GraphQL.newGraphQL(schema).build();
  }

  private GraphQLSchema buildSchema(String sdl) {
    TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
    RuntimeWiring runtimeWiring = buildWiring();
    SchemaGenerator schemaGenerator = new SchemaGenerator();
    return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
  }

  private RuntimeWiring buildWiring() {
    return RuntimeWiring.newRuntimeWiring()
      .scalar(ExtendedScalars.Date)
      .type(newTypeWiring(TYPE_QUERY)
        .dataFetcher("buchungById", graphQLDataFetchers.getBuchungByIdDataFetcher()))
      .type(newTypeWiring(TYPE_QUERY)
        .dataFetcher("buchungen", graphQLDataFetchers.getBuchungDataFetcher()))
      .type(newTypeWiring(TYPE_QUERY)
        .dataFetcher("saldo", graphQLDataFetchers.getSaldoDataFetcher()))
      .type(newTypeWiring(TYPE_QUERY)
        .dataFetcher("saldi", graphQLDataFetchers.getSaldiDataFetcher()))
      .type(newTypeWiring(TYPE_MUTATION)
        .dataFetcher("createUser", graphQLDataFetchers.createUser()))
      .type(newTypeWiring(TYPE_MUTATION)
        .dataFetcher("signIn", graphQLDataFetchers.signin()))
      .type(newTypeWiring(TYPE_MUTATION)
        .dataFetcher("refresh", graphQLDataFetchers.refresh()))
      .build();
  }
}
