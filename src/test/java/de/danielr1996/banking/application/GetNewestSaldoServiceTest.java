package de.danielr1996.banking.application;

import de.danielr1996.banking.domain.MockSaldoRepository;
import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.domain.exception.NewestSaldoNotFoundException;
import de.danielr1996.banking.domain.repository.SaldoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GetNewestSaldoServiceTest {

  private SaldoRepository saldoRepository = new MockSaldoRepository();

  private GetNewestSaldoService getNewestSaldoService = new GetNewestSaldoService(saldoRepository);

  @BeforeEach
  public void setup() {
    saldoRepository.deleteAll();
  }

  @Test
  void getNewestSaldoOfThree() throws NewestSaldoNotFoundException {
    UUID kontoId = UUID.randomUUID();
    Saldo saldoNeu = Saldo.builder().datum(LocalDateTime.now()).kontoId(kontoId).build();
    Saldo saldoMittel = Saldo.builder().datum(LocalDateTime.now().minusDays(1)).kontoId(kontoId).build();
    Saldo saldoAlt = Saldo.builder().datum(LocalDateTime.now().minusDays(2)).kontoId(kontoId).build();

    saldoRepository.save(saldoAlt);
    saldoRepository.save(saldoNeu);
    saldoRepository.save(saldoMittel);

    Saldo expected = saldoNeu;
    Saldo actual = getNewestSaldoService.getNewestSaldo(kontoId);
    assertEquals(expected.getId(), actual.getId());
  }

  @Test
  void getNewestSaldoOfOne() throws NewestSaldoNotFoundException {
    UUID kontoId = UUID.randomUUID();

    Saldo saldo = Saldo.builder()
      .kontoId(kontoId)
      .datum(LocalDateTime.now()).build();

    saldoRepository.save(saldo);

    Saldo expected = saldo;
    Saldo actual = getNewestSaldoService.getNewestSaldo(kontoId);
    assertEquals(expected.getId(), actual.getId());
  }

  @Test
  void getNewestSaldoOfNone() {
//    assertThrows(NoSuchElementException.class, () -> getNewestSaldoService.getNewestSaldo());
  }

  @AfterEach
  public void cleanup() {
    saldoRepository.deleteAll();
  }
}
