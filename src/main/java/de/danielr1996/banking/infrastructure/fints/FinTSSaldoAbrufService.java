package de.danielr1996.banking.infrastructure.fints;

import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.domain.services.SaldoAbrufService;
import lombok.extern.slf4j.Slf4j;
import org.kapott.hbci.GV_Result.GVRSaldoReq;
import org.kapott.hbci.exceptions.HBCI_Exception;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.passport.HBCIPassport;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
@Slf4j
@Profile("fints-prod")
public class FinTSSaldoAbrufService extends AbstractFinTSAbrufService implements SaldoAbrufService {
  private static final String JOB_NAME = "SaldoReq";

  // FIXME: Remove rpcId
  private GVRSaldoReq getSaldoReq(Konto konto, String rpcId) {
    HBCIPassport passport = this.getPassport(konto, rpcId);
    HBCIHandler handle = new HBCIHandler(VERSION.getId(), passport);
    org.kapott.hbci.structures.Konto k = chooseKonto(konto, passport);
    try {
      GVRSaldoReq saldo = getResult(handle, JOB_NAME, k);
      return saldo;
    } catch (HBCI_Exception e) {
      e.printStackTrace();
      return new GVRSaldoReq();
    } finally {
      handle.close();
      passport.close();
    }
  }


  // FIXME: Remove rpcId
  @Override
  public Saldo getSaldo(Konto konto, String rpcId) {
    org.kapott.hbci.structures.Saldo res = getSaldoReq(konto, rpcId).getEntries()[0].ready;
    return Saldo.builder()
      .betrag(res.value.getBigDecimalValue())
      .datum(res.timestamp.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime())
      .kontoId(konto.getId())
      .build();
  }
}
