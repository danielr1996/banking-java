package de.danielr1996.banking.domain.services;

import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.domain.entities.Saldo;
import org.springframework.stereotype.Service;

@Service
public interface SaldoAbrufService {
  Saldo getSaldo(Konto konto);
}
