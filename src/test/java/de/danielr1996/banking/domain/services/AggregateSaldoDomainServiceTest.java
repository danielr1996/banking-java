package de.danielr1996.banking.domain.services;

import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.entities.Saldo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class AggregateSaldoDomainServiceTest {
  @Test
  void aggregateSaldi() {
    LocalDateTime baseDate = LocalDateTime.of(2019, 10, 29, 22, 10, 1);
    Saldo currentSaldo = Saldo.builder().kontoId(UUID.randomUUID()).datum(baseDate).betrag(BigDecimal.valueOf(100)).build();
    List<Buchung> buchungen = Arrays.asList(
      Buchung.builder().kontoId(UUID.randomUUID()).valutadatum(baseDate.minusDays(1)).betrag(BigDecimal.valueOf(-60)).build(),
      Buchung.builder().kontoId(UUID.randomUUID()).valutadatum(baseDate.minusDays(2)).betrag(BigDecimal.valueOf(-10)).build(),
      Buchung.builder().kontoId(UUID.randomUUID()).valutadatum(baseDate.minusDays(3)).betrag(BigDecimal.valueOf(-10)).build(),
      Buchung.builder().kontoId(UUID.randomUUID()).valutadatum(baseDate.minusDays(4)).betrag(BigDecimal.valueOf(-20)).build(),
      Buchung.builder().kontoId(UUID.randomUUID()).valutadatum(baseDate.minusDays(5)).betrag(BigDecimal.valueOf(-20)).build(),
      Buchung.builder().kontoId(UUID.randomUUID()).valutadatum(baseDate.minusDays(6)).betrag(BigDecimal.valueOf(-20)).build(),
      Buchung.builder().kontoId(UUID.randomUUID()).valutadatum(baseDate.minusDays(7)).betrag(BigDecimal.valueOf(+40)).build()
    );

    List<Saldo> saldi = Arrays.asList(
      Saldo.builder().kontoId(UUID.randomUUID()).betrag(BigDecimal.valueOf(100)).datum(baseDate).build(),
      Saldo.builder().kontoId(UUID.randomUUID()).betrag(BigDecimal.valueOf(160)).datum(baseDate.minusDays(1)).build(),
      Saldo.builder().kontoId(UUID.randomUUID()).betrag(BigDecimal.valueOf(170)).datum(baseDate.minusDays(2)).build(),
      Saldo.builder().kontoId(UUID.randomUUID()).betrag(BigDecimal.valueOf(180)).datum(baseDate.minusDays(3)).build(),
      Saldo.builder().kontoId(UUID.randomUUID()).betrag(BigDecimal.valueOf(200)).datum(baseDate.minusDays(4)).build(),
      Saldo.builder().kontoId(UUID.randomUUID()).betrag(BigDecimal.valueOf(220)).datum(baseDate.minusDays(5)).build(),
      Saldo.builder().kontoId(UUID.randomUUID()).betrag(BigDecimal.valueOf(240)).datum(baseDate.minusDays(6)).build(),
      Saldo.builder().kontoId(UUID.randomUUID()).betrag(BigDecimal.valueOf(200)).datum(baseDate.minusDays(7)).build()
    );

    assertEquals(saldi, AggregateSaldoDomainService.aggregateSaldi(buchungen, currentSaldo));
  }
}
