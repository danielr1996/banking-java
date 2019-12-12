package de.danielr1996.banking.infrastructure.graphql.config;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;
import java.io.IOException;
import java.net.URL;
import javax.annotation.PostConstruct;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import de.danielr1996.banking.infrastructure.graphql.datafetchers.BuchungDataFetcher;
import de.danielr1996.banking.infrastructure.graphql.datafetchers.KontoDataFetcher;
import de.danielr1996.banking.infrastructure.graphql.datafetchers.RefreshDataFetcher;
import de.danielr1996.banking.infrastructure.graphql.datafetchers.SaldoDataFetcher;
import de.danielr1996.banking.infrastructure.graphql.datafetchers.UserDataFetcher;
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
  private final String TYPE_QUERY = "Query";
  private final String TYPE_MUTATION = "Mutation";

  @Autowired
  KontoDataFetcher kontoDataFetcher;

  @Autowired
  UserDataFetcher userDataFetcher;

  @Autowired
  SaldoDataFetcher saldoDataFetcher;

  @Autowired
  RefreshDataFetcher refreshDataFetcher;

  @Autowired
  BuchungDataFetcher buchungDataFetcher;

  @Bean
  public GraphQL graphQL() {
    return graphQL;
  }

  @PostConstruct
  public void init() throws IOException {
    URL url = Resources.getResource("graphql/schema.graphqls");
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
        .dataFetcher("buchungById", buchungDataFetcher.getBuchungByIdDataFetcher()))
      .type(newTypeWiring(TYPE_QUERY)
        .dataFetcher("buchungen", buchungDataFetcher.getBuchungDataFetcher()))
      .type(newTypeWiring(TYPE_QUERY)
        .dataFetcher("saldo", saldoDataFetcher.getSaldoDataFetcher()))
      .type(newTypeWiring(TYPE_QUERY)
        .dataFetcher("saldi", saldoDataFetcher.getSaldiDataFetcher()))
      .type(newTypeWiring(TYPE_QUERY)
        .dataFetcher("createUser", userDataFetcher.createUser()))
      .type(newTypeWiring(TYPE_QUERY)
        .dataFetcher("signIn", userDataFetcher.signin()))
      .type(newTypeWiring(TYPE_QUERY)
        .dataFetcher("refresh", refreshDataFetcher.refresh()))
      .type(newTypeWiring(TYPE_QUERY)
        .dataFetcher("konten", kontoDataFetcher.getKontoDataFetcher()))
      .type(newTypeWiring(TYPE_QUERY)
        .dataFetcher("createKonto", kontoDataFetcher.getCreateKontoDataFetcher()))
      .type(newTypeWiring(TYPE_QUERY)
        .dataFetcher("deleteKonto", kontoDataFetcher.getDeleteKontoDataFetcher()))
      .build();
  }
}
