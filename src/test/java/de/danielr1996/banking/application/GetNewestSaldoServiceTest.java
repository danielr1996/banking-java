package de.danielr1996.banking.application;

import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.repository.SaldoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class GetNewestSaldoServiceTest {

  @Autowired
  private SaldoRepository saldoRepository;

  @Autowired
  private GetNewestSaldoService getNewestSaldoService;

  @Test
  void getNewestSaldoOfThree() {
    Saldo saldoNeu = Saldo.builder().datum(LocalDate.now()).build();
    Saldo saldoMittel = Saldo.builder().datum(LocalDate.now().minusDays(1)).build();
    Saldo saldoAlt = Saldo.builder().datum(LocalDate.now().minusDays(2)).build();

    saldoRepository.save(saldoAlt);
    saldoRepository.save(saldoNeu);
    saldoRepository.save(saldoMittel);

    Saldo expected = saldoNeu;
    Saldo actual = getNewestSaldoService.getNewestSaldo();
    assertEquals(expected, actual);
  }

  @Test
  void getNewestSaldoOfOne() {
    Saldo saldo = Saldo.builder().datum(LocalDate.now()).build();

    saldoRepository.save(saldo);

    Saldo expected = saldo;
    Saldo actual = getNewestSaldoService.getNewestSaldo();
    assertEquals(expected, actual);
  }

  @Test
  void getNewestSaldoOfNone() {
    assertThrows(NoSuchElementException.class, () -> getNewestSaldoService.getNewestSaldo());
  }

  @AfterEach
  public void cleanup(){
    saldoRepository.deleteAll();
  }
}
