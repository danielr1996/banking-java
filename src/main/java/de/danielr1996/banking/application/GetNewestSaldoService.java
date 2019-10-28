package de.danielr1996.banking.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.UUID;
import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.domain.exception.NewestSaldoNotFoundException;
import de.danielr1996.banking.domain.repository.SaldoRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GetNewestSaldoService {

  @Autowired
  private SaldoRepository saldoRepository;

  public Saldo getNewestSaldo(UUID kontoId) {
    return saldoRepository
      .findAll()
      .stream()
      .filter(saldo->saldo.getKontoId().equals(kontoId))
      .max(Comparator.comparing(Saldo::getDatum))
      .orElseGet(() -> {
        Saldo lastSaldo = Saldo.builder()
          .datum(LocalDateTime.now())
          .betrag(BigDecimal.ZERO)
          .build();

        log.warn(MarkerFactory.getMarker(NewestSaldoNotFoundException.class.getName()), "Could not find newest Saldo, Assuming {}", lastSaldo);

        return lastSaldo;
      });
  }
}
