package de.danielr1996.banking.application;

import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.domain.exception.NewestSaldoNotFoundException;
import de.danielr1996.banking.domain.repository.SaldoRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;

@Service
@Slf4j
public class GetNewestSaldoService {

  @Autowired
  private SaldoRepository saldoRepository;

  public Saldo getNewestSaldo() {
    return saldoRepository
      .findAll()
      .stream()
      .max(Comparator.comparing(Saldo::getDatum))
      .orElseGet(() -> {
        Saldo lastSaldo = Saldo.builder()
          .datum(LocalDateTime.now())
          .betrag(BigDecimal.ZERO)
          .build();

        log.warn(MarkerFactory.getMarker(NewestSaldoNotFoundException.class.getName()), "Could not find newest Saldo, Assuming Saldo of {}", lastSaldo);

        return lastSaldo;
      });
  }
}
