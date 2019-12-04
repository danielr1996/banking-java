package de.danielr1996.banking.infrastructure.mock;

import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.domain.services.SaldoAbrufService;
import de.danielr1996.banking.infrastructure.fints.MyHBCICallback;
import org.kapott.hbci.callback.HBCICallback;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Primary
public class MockSaldoAbrufService implements SaldoAbrufService {

  @Override
  public Saldo getSaldo(Konto konto) {
    HBCICallback callback = new MyHBCICallback(konto.getBlz(), konto.getUserId(), konto.getPassword(), null, null);

    StringBuffer retData = new StringBuffer();
    callback.callback(null, HBCICallback.NEED_PT_TAN, "", 0, retData);
    System.out.println("Tan: " + retData);

    return Saldo.builder()
      .betrag(BigDecimal.TEN)
      .datum(LocalDateTime.now())
      .kontoId(UUID.randomUUID())
      .build();
  }
}
