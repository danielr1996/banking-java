package de.danielr1996.banking.infrastructure.mock;

import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.services.BuchungAbrufService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
public class MockUmsatzAbrufService implements BuchungAbrufService {
  @Override
  public Stream<Buchung> getBuchungen(Konto konto, Supplier<String> tanSp, Supplier<String> tanMediumSp) {
    return Stream.of(
      Buchung.builder()
        .betrag(BigDecimal.valueOf(-100))
        .build(),
      Buchung.builder()
        .betrag(BigDecimal.valueOf(200))
        .build()
    );
  }
}
