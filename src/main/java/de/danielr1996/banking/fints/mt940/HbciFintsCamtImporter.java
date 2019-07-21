package de.danielr1996.banking.fints.mt940;

import de.danielr1996.banking.domain.Buchung;
import de.danielr1996.banking.fints.HBCIPassportFactory;
import org.kapott.hbci.GV.HBCIJob;
import org.kapott.hbci.GV_Result.GVRKUms;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.manager.HBCIVersion;
import org.kapott.hbci.passport.HBCIPassport;
import org.kapott.hbci.status.HBCIExecStatus;
import org.kapott.hbci.structures.Konto;

import java.util.stream.Stream;

public class HbciFintsCamtImporter {
  private Konto self = getKonto();
  private final static HBCIVersion VERSION = HBCIVersion.HBCI_300;

  public Stream<Buchung> doImport() {
    return getUmsLines(self).map(new UmsLineUmsatzCaster().apply(self));
  }

  public Konto getKonto() {
    HBCIPassport passport = HBCIPassportFactory.getPassport();
    Konto[] konten = passport.getAccounts();
    Konto k = konten[3];
    return k;
  }

  public Stream<GVRKUms.UmsLine> getUmsLines(Konto self) {
    System.out.println(self);
    HBCIHandler handle = null;
    HBCIPassport passport = HBCIPassportFactory.getPassport();
    try {
      handle = new HBCIHandler(VERSION.getId(), passport);
      HBCIJob umsatzJob = handle.newJob("KUmsAllCamt");
      umsatzJob.setParam("my", self); // festlegen, welches Konto abgefragt werden soll.
      // "Konto" ausgeben da die Klasse anscheinend Lazy ist und der gesetzte Parameter sonst leer ist
//      System.out.println(self.bic);
      umsatzJob.setParam("my.bic", self.bic); // festlegen, welches Konto abgefragt werden soll.
      umsatzJob.addToQueue(); // Zur Liste der auszufuehrenden Auftraege hinzufuegen
      HBCIExecStatus status = handle.execute();
      if (!status.isOK()) {
        System.err.println(status.toString());
        return Stream.empty();
      }
      GVRKUms result = (GVRKUms) umsatzJob.getJobResult();
      if (!result.isOK()) {
        System.err.println(result.toString());
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
}
