package de.danielr1996.banking.infrastructure.mock;

import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.domain.repository.SaldoRepository;
import de.danielr1996.banking.domain.services.SaldoAbrufService;
import de.danielr1996.banking.infrastructure.fints.ConsoleHBCICallback;
import de.danielr1996.banking.infrastructure.fints.HBCICallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.kapott.hbci.callback.HBCICallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Profile("fints-mock")
@Slf4j
public class MockSaldoAbrufService implements SaldoAbrufService {

  private HBCICallbackFactory hbciCallbackFactory;

  @Autowired
  SaldoRepository saldoRepository;

  @Autowired
  private MockSaldoAbrufService(HBCICallbackFactory hbciCallbackFactory){
    this.hbciCallbackFactory = hbciCallbackFactory;
  }

  @Override
  public Saldo getSaldo(Konto konto, String rpcId) {
    HBCICallback hbciCallback = hbciCallbackFactory.getCallBack(konto, rpcId);

    StringBuffer tan = new StringBuffer();
    hbciCallback.callback(null, HBCICallback.NEED_PT_TAN,"MockSaldoAbrufService benötigt TAN",0,tan);
    log.info("MockSaldoAbrufService hat TAN erhalten: {}", tan);
    Saldo saldo = Saldo.builder()
//      .id(UUID.randomUUID())
      .betrag(BigDecimal.valueOf(100))
      .datum(LocalDateTime.now())
      .kontoId(konto.getId())
      .build();

    Saldo currentSaldo = saldoRepository.findByKontoId(konto.getId());
    return currentSaldo.substract(saldo);
  }
}
