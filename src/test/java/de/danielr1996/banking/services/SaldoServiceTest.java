package de.danielr1996.banking.services;

import de.danielr1996.banking.domain.services.AggregateSaldoService;
import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.entities.Saldo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AggregateSaldoServiceTest {

  @Test
  void aggregateSaldi() {
    Saldo currentSaldo = Saldo.builder().datum(LocalDateTime.now()).betrag(BigDecimal.valueOf(100)).build();

    List<Buchung> buchungen = Arrays.asList(
      Buchung.builder().valutadatum(LocalDateTime.now().minusDays(1)).betrag(BigDecimal.valueOf(-60)).build(),
      Buchung.builder().valutadatum(LocalDateTime.now().minusDays(2)).betrag(BigDecimal.valueOf(-10)).build(),
      Buchung.builder().valutadatum(LocalDateTime.now().minusDays(3)).betrag(BigDecimal.valueOf(-10)).build(),
      Buchung.builder().valutadatum(LocalDateTime.now().minusDays(4)).betrag(BigDecimal.valueOf(-20)).build(),
      Buchung.builder().valutadatum(LocalDateTime.now().minusDays(5)).betrag(BigDecimal.valueOf(-20)).build(),
      Buchung.builder().valutadatum(LocalDateTime.now().minusDays(6)).betrag(BigDecimal.valueOf(-20)).build(),
      Buchung.builder().valutadatum(LocalDateTime.now().minusDays(7)).betrag(BigDecimal.valueOf(+40)).build()
    );

    List<Saldo> saldi = Arrays.asList(
      Saldo.builder().betrag(BigDecimal.valueOf(100)).datum(LocalDateTime.now()).build(),
      Saldo.builder().betrag(BigDecimal.valueOf(160)).datum(LocalDateTime.now().minusDays(1)).build(),
      Saldo.builder().betrag(BigDecimal.valueOf(170)).datum(LocalDateTime.now().minusDays(2)).build(),
      Saldo.builder().betrag(BigDecimal.valueOf(180)).datum(LocalDateTime.now().minusDays(3)).build(),
      Saldo.builder().betrag(BigDecimal.valueOf(200)).datum(LocalDateTime.now().minusDays(4)).build(),
      Saldo.builder().betrag(BigDecimal.valueOf(220)).datum(LocalDateTime.now().minusDays(5)).build(),
      Saldo.builder().betrag(BigDecimal.valueOf(240)).datum(LocalDateTime.now().minusDays(6)).build(),
      Saldo.builder().betrag(BigDecimal.valueOf(200)).datum(LocalDateTime.now().minusDays(7)).build()
    );

    assertEquals(saldi, AggregateSaldoService.aggregateSaldi(null,buchungen, currentSaldo));
  }
}
