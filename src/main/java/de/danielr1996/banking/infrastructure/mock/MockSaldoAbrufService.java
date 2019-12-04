package de.danielr1996.banking.infrastructure.mock;

import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.domain.services.SaldoAbrufService;
import de.danielr1996.banking.infrastructure.fints.ConsoleHBCICallback;
import org.kapott.hbci.callback.HBCICallback;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Profile("fints-mock")
public class MockSaldoAbrufService implements SaldoAbrufService {

  @Override
  public Saldo getSaldo(Konto konto, String rpcId) {
    return Saldo.builder()
      .id(UUID.randomUUID())
      .betrag(BigDecimal.TEN)
      .datum(LocalDateTime.now())
      .kontoId(konto.getId())
      .build();
  }
}
