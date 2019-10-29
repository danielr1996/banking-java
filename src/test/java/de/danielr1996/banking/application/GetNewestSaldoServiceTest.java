package de.danielr1996.banking.application;

import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.domain.exception.NewestSaldoNotFoundException;
import de.danielr1996.banking.domain.repository.SaldoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class GetNewestSaldoServiceTest {

  @Autowired
  private SaldoRepository saldoRepository;

  @Autowired
  private GetNewestSaldoService getNewestSaldoService;

  @BeforeEach
  public void setup(){
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
  public void cleanup(){
    saldoRepository.deleteAll();
  }
}
