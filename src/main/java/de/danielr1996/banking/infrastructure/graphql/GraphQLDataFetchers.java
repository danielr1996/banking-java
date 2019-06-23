package de.danielr1996.banking.infrastructure.graphql;

import com.google.common.collect.ImmutableMap;
import de.danielr1996.banking.repository.BuchungRepository;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class GraphQLDataFetchers {
    @Autowired
    private BuchungRepository buchungRepository;

    private static List<Map<String, String>> books = Arrays.asList(
            ImmutableMap.of("id", "book-1",
                    "name", "Harry Potter and the Philosopher's Stone",
                    "pageCount", "223"),
            ImmutableMap.of("id", "book-2",
                    "name", "Moby Dick",
                    "pageCount", "635"),
            ImmutableMap.of("id", "book-3",
                    "name", "Interview with the vampire",
                    "pageCount", "371")
    );

    public DataFetcher getBookByIdDataFetcher(){
        return dataFetchingEnvironment -> {
            String bookId = dataFetchingEnvironment.getArgument("id");

            return books
                    .stream()
                    .filter(book->book.get("id").equals(bookId))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher getBuchungByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            String buchungId = dataFetchingEnvironment.getArgument("id");

            return buchungRepository.findById(UUID.fromString(buchungId));
        };
    }

    public DataFetcher getBuchungDataFetcher() {
        return dataFetchingEnvironment -> {
            return buchungRepository.findAll();
        };
    }
}
