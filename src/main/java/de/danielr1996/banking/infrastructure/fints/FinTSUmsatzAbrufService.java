package de.danielr1996.banking.infrastructure.fints;

import java.io.File;
import java.time.ZoneId;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.domain.entities.TransaktionsPartner;
import de.danielr1996.banking.domain.services.BuchungAbrufService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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


@Service
@Primary
@Slf4j
public class FinTSUmsatzAbrufService implements BuchungAbrufService {
  private final static HBCIVersion VERSION = HBCIVersion.HBCI_300;

  private UmsatzAbrufResponse getUmsaetze(Konto konto, String rpcId) {
    Properties props = new Properties();
    HBCIUtils.init(props, new WampHBCICallback(konto.getBlz(), konto.getKontonummer(), konto.getPassword(), rpcId));
    final File passportFile = new File("user-"+konto.getId() + ".dat");
//    final File passportFile = new File(UUID.randomUUID().toString()+".cat");
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

//      log.info("Anzahl Konten: {}", konten.length);
      org.kapott.hbci.structures.Konto k = konten[3];
//      System.out.println("Konto: " + k.bic);
      log.info("Using {}", k);
      HBCIJob umsatzJob = handle.newJob("KUmsAllCamt");
      umsatzJob.setParam("my", k); // festlegen, welches Konto abgefragt werden soll.
      umsatzJob.setParam("my.bic", k.bic); // festlegen, welches Konto abgefragt werden soll.
      umsatzJob.addToQueue(); // Zur Liste der auszufuehrenden Auftraege hinzufuegen


      HBCIExecStatus status = handle.execute();

      if (!status.isOK())
        log.error("{}", status);

      GVRKUms result = (GVRKUms) umsatzJob.getJobResult();

      if (!result.isOK())
        log.error("{}", result);

      // Alle Umsatzbuchungen ausgeben
      List<GVRKUms.UmsLine> umsaetze = result.getFlatData();

      return UmsatzAbrufResponse.builder()
        .umsaetze(umsaetze)
        .konto(k)
        .build();

    } finally {
      // Sicherstellen, dass sowohl Passport als auch Handle nach Beendigung geschlossen werden.
      if (handle != null)
        handle.close();

      if (passport != null)
        passport.close();
    }
  }

  @Override
  public Stream<Buchung> getBuchungen(Konto konto, String rpcId) {
    UmsatzAbrufResponse res = getUmsaetze(konto, rpcId);
    org.kapott.hbci.structures.Konto self = res.getKonto();

    return res.getUmsaetze().stream().map(umsLine -> {
      TransaktionsPartner otherPartner = null;
      if(umsLine.other.iban != null){
        otherPartner=TransaktionsPartner.builder()
          .bic(umsLine.other.bic)
          .blz(umsLine.other.blz)
          .iban(umsLine.other.iban)
          .name(umsLine.other.name)
          .build();
      }
      return Buchung.builder()
        .id(umsLine.id)
        .buchungstag(umsLine.bdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
        .valutadatum(umsLine.valuta.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
        .waehrung(umsLine.value.getCurr())
        .buchungstext(umsLine.text)
        .verwendungszweck(String.join("", umsLine.usage))
        .betrag(umsLine.value.getBigDecimalValue())
        .otherPartner(otherPartner)
        .selfPartner(TransaktionsPartner.builder()
          .iban(self.iban)
          .bic(self.bic)
          .name(self.name)
          .blz(self.blz)
          .build())
        .build();
    });
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  private static class UmsatzAbrufResponse {
    private List<GVRKUms.UmsLine> umsaetze;
    private org.kapott.hbci.structures.Konto konto;
  }
}
