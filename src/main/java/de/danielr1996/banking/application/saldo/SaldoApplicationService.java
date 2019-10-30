package de.danielr1996.banking.application.saldo;

import java.util.UUID;
import de.danielr1996.banking.application.auth.OwnershipService;
import de.danielr1996.banking.domain.entities.Saldo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaldoApplicationService {
  @Autowired
  GetNewestSaldoService getNewestSaldoService;
  @Autowired
  OwnershipService ownershipService;

  public Saldo getNewestSaldo(UUID requesterId, UUID kontoId) {
      Saldo saldo = getNewestSaldoService.getNewestSaldo(kontoId);
      if(ownershipService.isOwner(requesterId, saldo)){
        return saldo;
      }else{
        throw new RuntimeException("User not autohrized");
      }
  }
}
