package de.danielr1996.banking.services;

import de.danielr1996.banking.domain.Buchung;
import de.danielr1996.banking.domain.Saldo;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SaldoServiceTest {

  @Test
  void getSaldos() {
    Saldo currentSaldo = Saldo.builder().datum(LocalDate.now()).betrag(BigDecimal.valueOf(100)).build();

    List<Buchung> buchungen = Arrays.asList(
      Buchung.builder().valutadatum(LocalDate.now().minusDays(1)).betrag(BigDecimal.valueOf(-60)).build(),
      Buchung.builder().valutadatum(LocalDate.now().minusDays(2)).betrag(BigDecimal.valueOf(-10)).build(),
      Buchung.builder().valutadatum(LocalDate.now().minusDays(3)).betrag(BigDecimal.valueOf(-10)).build(),
      Buchung.builder().valutadatum(LocalDate.now().minusDays(4)).betrag(BigDecimal.valueOf(-20)).build(),
      Buchung.builder().valutadatum(LocalDate.now().minusDays(5)).betrag(BigDecimal.valueOf(-20)).build(),
      Buchung.builder().valutadatum(LocalDate.now().minusDays(6)).betrag(BigDecimal.valueOf(-20)).build(),
      Buchung.builder().valutadatum(LocalDate.now().minusDays(7)).betrag(BigDecimal.valueOf(+40)).build()
    );

    List<Saldo> saldi = Arrays.asList(
      Saldo.builder().betrag(BigDecimal.valueOf(100)).datum(LocalDate.now()).build(),
      Saldo.builder().betrag(BigDecimal.valueOf(160)).datum(LocalDate.now().minusDays(1)).build(),
      Saldo.builder().betrag(BigDecimal.valueOf(170)).datum(LocalDate.now().minusDays(2)).build(),
      Saldo.builder().betrag(BigDecimal.valueOf(180)).datum(LocalDate.now().minusDays(3)).build(),
      Saldo.builder().betrag(BigDecimal.valueOf(200)).datum(LocalDate.now().minusDays(4)).build(),
      Saldo.builder().betrag(BigDecimal.valueOf(220)).datum(LocalDate.now().minusDays(5)).build(),
      Saldo.builder().betrag(BigDecimal.valueOf(240)).datum(LocalDate.now().minusDays(6)).build(),
      Saldo.builder().betrag(BigDecimal.valueOf(200)).datum(LocalDate.now().minusDays(7)).build()
    );

    assertEquals(saldi, SaldoService.getSaldi(buchungen, currentSaldo));
  }
}
