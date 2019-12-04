package de.danielr1996.banking.infrastructure.mock;

import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.services.BuchungAbrufService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
@Profile("fints-mock")
public class MockUmsatzAbrufService implements BuchungAbrufService {

  // TODO: Remove rpcId
  @Override
  public Stream<Buchung> getBuchungen(Konto konto, String rpcId) {
    return Stream.of(
      Buchung.builder()
        .id(UUID.randomUUID().toString())
        .betrag(BigDecimal.valueOf(-100))
        .kontoId(konto.getId())
        .build(),
      Buchung.builder()
        .id(UUID.randomUUID().toString())
        .betrag(BigDecimal.valueOf(200))
        .kontoId(konto.getId())
        .build()
    );
  }
}
