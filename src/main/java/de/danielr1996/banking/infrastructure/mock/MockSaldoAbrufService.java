package de.danielr1996.banking.infrastructure.mock;

import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.domain.services.SaldoAbrufService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class MockSaldoAbrufService implements SaldoAbrufService {
  @Override
  public Saldo getSaldo(Konto konto) {
    return Saldo.builder()
      .betrag(BigDecimal.TEN)
      .datum(LocalDate.now())
      .ownerid(UUID.randomUUID())
      .build();
  }
}
