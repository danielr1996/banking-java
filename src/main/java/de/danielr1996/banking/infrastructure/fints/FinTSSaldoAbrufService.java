package de.danielr1996.banking.infrastructure.fints;

import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.domain.services.SaldoAbrufService;
import lombok.extern.slf4j.Slf4j;
import org.kapott.hbci.GV.HBCIJob;
import org.kapott.hbci.GV_Result.GVRSaldoReq;
import org.kapott.hbci.manager.BankInfo;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.manager.HBCIUtils;
import org.kapott.hbci.manager.HBCIVersion;
import org.kapott.hbci.passport.AbstractHBCIPassport;
import org.kapott.hbci.passport.HBCIPassport;
import org.kapott.hbci.status.HBCIExecStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.ZoneId;
import java.util.Properties;
import java.util.Scanner;

@Service
@Slf4j
@Profile("fints-prod")
public class FinTSSaldoAbrufService implements SaldoAbrufService {
  private final static HBCIVersion VERSION = HBCIVersion.HBCI_300;

  private HBCICallbackFactory hbciCallbackFactory;

  @Autowired
  private FinTSSaldoAbrufService(HBCICallbackFactory hbciCallbackFactory) {
    this.hbciCallbackFactory = hbciCallbackFactory;
  }

  // FIXME: Remove rpcId
  private GVRSaldoReq getSaldoReq(Konto konto, String rpcId) {
    Properties props = new Properties();
    HBCIUtils.init(props, hbciCallbackFactory.getCallBack(konto.getBlz(), konto.getKontonummer(), konto.getPasswordhash(), rpcId));
    final File passportFile = new File("user-" + konto.getId() + ".dat");
    HBCIUtils.setParam("client.passport.default", "PinTan"); // Legt als Verfahren PIN/TAN fest.
    HBCIUtils.setParam("client.passport.PinTan.init", "1"); // Stellt sicher, dass der Passport initialisiert wird
    HBCIPassport passport = AbstractHBCIPassport.getInstance(passportFile);
    passport.setCountry("DE");
    BankInfo info = HBCIUtils.getBankInfo(konto.getBlz());
    passport.setHost(info.getPinTanAddress());
    passport.setPort(443);
    passport.setFilterType("Base64");

    HBCIHandler handle = null;

    try {
      handle = new HBCIHandler(VERSION.getId(), passport);

      org.kapott.hbci.structures.Konto[] konten = passport.getAccounts();
      if (konten == null || konten.length == 0)
        log.error("Keine Konten ermittelbar");

      // FIXME: auslagern
      org.kapott.hbci.structures.Konto k = konten[3];
      HBCIJob umsatzJob = handle.newJob("SaldoReq");
      umsatzJob.setParam("my", k); // festlegen, welches Konto abgefragt werden soll.

      umsatzJob.addToQueue(); // Zur Liste der auszufuehrenden Auftraege hinzufuegen


      HBCIExecStatus status = handle.execute();

      if (!status.isOK())
        log.error("{}", status);

      GVRSaldoReq result = (GVRSaldoReq) umsatzJob.getJobResult();

      if (!result.isOK())
        log.error("{}", result);

      return result;

    } finally {
      // Sicherstellen, dass sowohl Passport als auch Handle nach Beendigung geschlossen werden.
      if (handle != null)
        handle.close();

      if (passport != null)
        passport.close();
    }
  }


  // FIXME: Remove rpcId
  @Override
  public Saldo getSaldo(Konto konto, String rpcId) {
    org.kapott.hbci.structures.Saldo res = getSaldoReq(konto, rpcId).getEntries()[0].ready;
    Saldo saldo = Saldo.builder()
      .betrag(res.value.getBigDecimalValue())
      .datum(res.timestamp.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime())
      .kontoId(konto.getId())
      .build();
    return saldo;
  }
}
