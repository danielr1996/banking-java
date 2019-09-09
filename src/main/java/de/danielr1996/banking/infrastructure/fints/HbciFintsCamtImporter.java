package de.danielr1996.banking.infrastructure.fints;

import de.danielr1996.banking.domain.entities.Buchung;
import lombok.extern.slf4j.Slf4j;
import org.kapott.hbci.GV.HBCIJob;
import org.kapott.hbci.GV_Result.GVRKUms;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.manager.HBCIVersion;
import org.kapott.hbci.passport.HBCIPassport;
import org.kapott.hbci.status.HBCIExecStatus;
import org.kapott.hbci.structures.Konto;

import java.time.ZoneId;
import java.util.function.Function;
import java.util.stream.Stream;

@Slf4j
public class HbciFintsCamtImporter {
  private HBCIPassport passport = UmsatzAbrufPinTan.getPassport();
  private Konto self = getKonto(this.passport);

  private final static HBCIVersion VERSION = HBCIVersion.HBCI_300;


  @Deprecated
  public Konto getKonto(HBCIPassport passport) {
    Konto[] konten = passport.getAccounts();

    Konto k = konten[3];
    return k;
  }


  public Stream<Buchung> doImport() {
    return getUmsLines(self).map(caster.apply(self));
  }

  private Stream<GVRKUms.UmsLine> getUmsLines(Konto self) {
    log.info("Using konto {}", self);
    HBCIHandler handle = null;
    HBCIPassport passport = this.passport;
    try {
      handle = new HBCIHandler(VERSION.getId(), passport);
      HBCIJob umsatzJob = handle.newJob("KUmsAllCamt");

      umsatzJob.setParam("my.bic", "SSKNDE77XXX"); // festlegen, welches Konto abgefragt werden soll.
      umsatzJob.setParam("my.iban", "DE45760501010005182225"); // festlegen, welches Konto abgefragt werden soll.
      umsatzJob.addToQueue(); // Zur Liste der auszufuehrenden Auftraege hinzufuegen

      HBCIExecStatus status = handle.execute();
      if (!status.isOK()) {
        log.error(status.toString());
        return Stream.empty();
      }
      GVRKUms result = (GVRKUms) umsatzJob.getJobResult();
      if (!result.isOK()) {
        log.error(result.toString());
        return Stream.empty();
      }
      return result.getFlatData().stream();
    } finally {
      if (handle != null)
        handle.close();

      if (passport != null)
        passport.close();
    }
  }

  private Function<Konto, Function<GVRKUms.UmsLine, Buchung>> caster = (Konto konto) -> umsLine -> Buchung.builder()
    .id(umsLine.id)
    .buchungstag(umsLine.bdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
    .valutadatum(umsLine.valuta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
    .waehrung(umsLine.value.getCurr())
    .buchungstext(umsLine.text)
    .verwendungszweck(String.join("", umsLine.usage))
    .betrag(umsLine.value.getBigDecimalValue())
    /*.otherPartner(TransaktionsPartner.builder()
      .id(UUID.randomUUID())
//          .iban(umsLine.other.iban)
      .iban(UUID.randomUUID().toString())
      .bic(umsLine.other.bic)
      .name(umsLine.other.name)
      .build())
    .selfPartner(TransaktionsPartner.builder()
      .id(UUID.randomUUID())
//          .iban(self.iban)
      .iban(UUID.randomUUID().toString())
      .bic(self.bic)
      .name(self.name)
      .build())*/
    .build();
}
