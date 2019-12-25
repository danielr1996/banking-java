package de.danielr1996.banking.domain.services;

import java.util.UUID;
import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.domain.entities.Saldo;
import org.springframework.stereotype.Service;

@Service
public interface SaldoAbrufService {
  // FIXME: Remove rpcId
  Saldo getSaldo(Konto kontoId, String rpcId);
}
