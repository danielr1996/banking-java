package de.danielr1996.banking.domain.entities;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@Tag("unit")
class SaldoTest {

  @Test
  void saldo1AddSaldo2() {
    LocalDateTime now = LocalDateTime.now();
    UUID kontoId = UUID.randomUUID();
    Saldo saldo1 = Saldo.builder()
      .betrag(BigDecimal.TEN)
      .datum(now)
      .kontoId(kontoId)
      .build();

    Saldo saldo2 = Saldo.builder()
      .betrag(BigDecimal.ONE)
      .datum(now.minusDays(1))
      .kontoId(kontoId)
      .build();

    Saldo expected = Saldo.builder()
      .betrag(BigDecimal.valueOf(11))
      .datum(now)
      .kontoId(kontoId)
      .build();

    Saldo actual = saldo1.add(saldo2);

    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  void saldo2AddSaldo1() {
    LocalDateTime now = LocalDateTime.now();
    UUID kontoId = UUID.randomUUID();
    Saldo saldo1 = Saldo.builder()
      .betrag(BigDecimal.TEN)
      .datum(now)
      .kontoId(kontoId)
      .build();

    Saldo saldo2 = Saldo.builder()
      .betrag(BigDecimal.ONE)
      .datum(now.minusDays(1))
      .kontoId(kontoId)
      .build();

    Saldo expected = Saldo.builder()
      .betrag(BigDecimal.valueOf(11))
      .datum(now)
      .kontoId(kontoId)
      .build();

    Saldo actual = saldo2.add(saldo1);

    assertThat(actual, is(equalTo(expected)));
  }
}
