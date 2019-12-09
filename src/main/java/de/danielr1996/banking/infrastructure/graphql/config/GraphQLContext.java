package de.danielr1996.banking.infrastructure.graphql.config;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GraphQLContext {
  private Optional<String> jwt;
}
