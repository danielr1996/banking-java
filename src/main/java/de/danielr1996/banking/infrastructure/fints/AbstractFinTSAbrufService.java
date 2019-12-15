package de.danielr1996.banking.infrastructure.fints;

import de.danielr1996.banking.domain.entities.Konto;
import lombok.extern.slf4j.Slf4j;
import org.kapott.hbci.GV.HBCIJob;
import org.kapott.hbci.GV_Result.GVRKUms;
import org.kapott.hbci.GV_Result.GVRSaldoReq;
import org.kapott.hbci.GV_Result.HBCIJobResult;
import org.kapott.hbci.GV_Result.HBCIJobResultImpl;
import org.kapott.hbci.manager.BankInfo;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.manager.HBCIUtils;
import org.kapott.hbci.manager.HBCIVersion;
import org.kapott.hbci.passport.AbstractHBCIPassport;
import org.kapott.hbci.passport.HBCIPassport;
import org.kapott.hbci.status.HBCIExecStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Slf4j
public abstract class AbstractFinTSAbrufService {

  @Autowired
  protected HBCICallbackFactory hbciCallbackFactory;

  protected final static HBCIVersion VERSION = HBCIVersion.HBCI_300;


  // FIXME: Remove rpcId
  protected HBCIPassport getPassport(Konto konto, String rpcId) {
    Properties props = new Properties();
    HBCIUtils.init(props, this.hbciCallbackFactory.getCallBack(konto, rpcId));
    final File passportFile = new File("konto-" + konto.getId() + ".dat");
    HBCIUtils.setParam("client.passport.default", "PinTan"); // Legt als Verfahren PIN/TAN fest.
    HBCIUtils.setParam("client.passport.PinTan.init", "1"); // Stellt sicher, dass der Passport initialisiert wird
    HBCIPassport passport = AbstractHBCIPassport.getInstance(passportFile);
    passport.setCountry("DE");
    BankInfo info = HBCIUtils.getBankInfo(konto.getBlz());
    passport.setHost(info.getPinTanAddress());
    passport.setPort(443);
    passport.setFilterType("Base64");

    return passport;
  }

  protected org.kapott.hbci.structures.Konto chooseKonto(Konto konto, HBCIPassport passport) {
    org.kapott.hbci.structures.Konto[] konten = passport.getAccounts();

    if (konten == null || konten.length == 0) {
      log.error("Keine Konten ermittelbar");
      throw new RuntimeException("Not Found");
    } else {
      org.kapott.hbci.structures.Konto k =
        Arrays
          .stream(konten)
          .filter(k1 -> {
            return k1.number.equals(konto.getKontonummer());
          })
          .findFirst()
          .orElseThrow(() -> new RuntimeException("Not Found"));

      log.info("Using {}", k);
      return k;
    }
  }

  protected <T extends HBCIJobResult> T getResult(HBCIHandler handle, String jobname, org.kapott.hbci.structures.Konto k) {
    HBCIJob umsatzJob = handle.newJob(jobname);
    umsatzJob.setParam("my", k); // festlegen, welches Konto abgefragt werden soll.
    umsatzJob.addToQueue(); // Zur Liste der auszufuehrenden Auftraege hinzufuegen
    HBCIExecStatus status = handle.execute();

    if (!status.isOK())
      log.error("{}", status);

    @SuppressWarnings("unchecked")
    T result = (T) umsatzJob.getJobResult();
    if (!result.isOK())
      log.error("{}", result);
    return result;
  }

  /*protected GVRSaldoReq getResult2(HBCIHandler handle, String jobname, org.kapott.hbci.structures.Konto k) {
    HBCIJob saldoJob = handle.newJob(jobname);
    saldoJob.setParam("my", k); // festlegen, welches Konto abgefragt werden soll.
    saldoJob.addToQueue(); // Zur Liste der auszufuehrenden Auftraege hinzufuegen
    HBCIExecStatus status = handle.execute();

    if (!status.isOK())
      log.error("{}", status);

    GVRSaldoReq result = (GVRSaldoReq) saldoJob.getJobResult();

    if (!result.isOK())
      log.error("{}", result);

    return result;
  }*/

 /* protected GVRKUms getResult(){
    if (!status.isOK())
      log.error("{}", status);

    GVRKUms result = (GVRKUms) umsatzJob.getJobResult();

    if (!result.isOK())
      log.error("{}", result);

    // Alle Umsatzbuchungen ausgeben
    List<GVRKUms.UmsLine> umsaetze = result.getFlatData();
  }*/
}
