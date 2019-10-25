package de.danielr1996.banking.infrastructure.fints;

import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.services.BuchungAbrufService;
import lombok.extern.slf4j.Slf4j;
import org.kapott.hbci.GV.HBCIJob;
import org.kapott.hbci.GV_Result.GVRKUms;
import org.kapott.hbci.manager.BankInfo;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.manager.HBCIUtils;
import org.kapott.hbci.manager.HBCIVersion;
import org.kapott.hbci.passport.AbstractHBCIPassport;
import org.kapott.hbci.passport.HBCIPassport;
import org.kapott.hbci.status.HBCIExecStatus;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.ZoneId;
import java.util.List;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.stream.Stream;


@Service
@Primary
@Slf4j
public class FinTSUmsatzAbrufService implements BuchungAbrufService {
  private final static HBCIVersion VERSION = HBCIVersion.HBCI_300;

  private List<GVRKUms.UmsLine> getUmsaetze(Konto konto, Supplier<String> tanSp, Supplier<String> tanMediumSp) {
    Properties props = new Properties();
    HBCIUtils.init(props, new MyHBCICallback(konto.getBlz(), konto.getKontonummer(), konto.getPassword(), tanSp, tanMediumSp));
    final File passportFile = new File(konto.getId() + ".dat");
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

      log.info("Anzahl Konten: {}", konten.length);
      org.kapott.hbci.structures.Konto k = konten[3];

      HBCIJob umsatzJob = handle.newJob("KUmsAllCamt");
      umsatzJob.setParam("my", k); // festlegen, welches Konto abgefragt werden soll.
      umsatzJob.addToQueue(); // Zur Liste der auszufuehrenden Auftraege hinzufuegen


      HBCIExecStatus status = handle.execute();

      if (!status.isOK())
        log.error("{}", status);

      GVRKUms result = (GVRKUms) umsatzJob.getJobResult();

      if (!result.isOK())
        log.error("{}", result);

      // Alle Umsatzbuchungen ausgeben
      List<GVRKUms.UmsLine> buchungen = result.getFlatData();
      return buchungen;
    } finally {
      // Sicherstellen, dass sowohl Passport als auch Handle nach Beendigung geschlossen werden.
      if (handle != null)
        handle.close();

      if (passport != null)
        passport.close();
    }
  }

  @Override
  public Stream<Buchung> getBuchungen(Konto konto, Supplier<String> tanSp, Supplier<String> tanMediumSp) {
    return getUmsaetze(konto, tanSp, tanMediumSp).stream().map(umsLine -> Buchung.builder()
      .id(umsLine.id)
      .buchungstag(umsLine.bdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
      .valutadatum(umsLine.valuta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
      .waehrung(umsLine.value.getCurr())
      .buchungstext(umsLine.text)
      .verwendungszweck(String.join("", umsLine.usage))
      .betrag(umsLine.value.getBigDecimalValue())
      .build());
  }
}
