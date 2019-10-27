package de.danielr1996.banking.infrastructure.fints;

import de.danielr1996.banking.domain.entities.Saldo;
import lombok.extern.slf4j.Slf4j;
import org.kapott.hbci.GV.HBCIJob;
import org.kapott.hbci.GV_Result.GVRSaldoReq;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.manager.HBCIVersion;
import org.kapott.hbci.passport.HBCIPassport;
import org.kapott.hbci.status.HBCIExecStatus;
import org.kapott.hbci.structures.Konto;

import java.time.ZoneId;
import java.util.function.Function;

@Slf4j
public class SaldoImporter {
  private HBCIPassport passport = null;
//  private HBCIPassport passport = UmsatzAbrufPinTan.getPassport();
  private Konto self = getKonto(this.passport);
  private final static HBCIVersion VERSION = HBCIVersion.HBCI_300;

  public Saldo doImport() {
    return caster.apply(getSaldo(self));
  }

  @Deprecated
  public Konto getKonto(HBCIPassport passport) {
    Konto[] konten = passport.getAccounts();

    Konto k = konten[3];
    return k;
  }

  private GVRSaldoReq getSaldo(Konto self) {
    log.info("Using konto {}", self);
    HBCIHandler handle = null;
    HBCIPassport passport = this.passport;
    try {
      handle = new HBCIHandler(VERSION.getId(), passport);
      HBCIJob umsatzJob = handle.newJob("SaldoReq");

//      umsatzJob.setParam("my.bic", "SSKNDE77XXX"); // festlegen, welches Konto abgefragt werden soll.
//      umsatzJob.setParam("my.iban", "DE45760501010005182225"); // festlegen, welches Konto abgefragt werden soll.
      umsatzJob.setParam("my", self);
      umsatzJob.addToQueue(); // Zur Liste der auszufuehrenden Auftraege hinzufuegen

      HBCIExecStatus status = handle.execute();
      if (!status.isOK()) {
        log.error(status.toString());
//        return Stream.empty();
      }
      GVRSaldoReq result = (GVRSaldoReq) umsatzJob.getJobResult();
      if (!result.isOK()) {
        log.error(result.toString());
//        return Stream.empty();
      }
//      return result.getR;
//      System.out.println(result.getEntries());
      return result;
    } finally {
      if (handle != null)
        handle.close();

      if (passport != null)
        passport.close();
    }
  }

  private Function<GVRSaldoReq, Saldo> caster = saldo -> Saldo.builder()
    .betrag(saldo.getEntries()[0].ready.value.getBigDecimalValue())
//    .datum(saldo.getEntries()[0].ready.timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
    .build();
}
