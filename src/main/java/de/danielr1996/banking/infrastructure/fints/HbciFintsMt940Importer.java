package de.danielr1996.banking.infrastructure.fints;

import de.danielr1996.banking.domain.entities.Buchung;
import org.kapott.hbci.GV.HBCIJob;
import org.kapott.hbci.GV_Result.GVRKUms;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.manager.HBCIVersion;
import org.kapott.hbci.structures.Konto;

import java.util.stream.Stream;

@Deprecated
public class HbciFintsMt940Importer {

    private final static HBCIVersion VERSION = HBCIVersion.HBCI_300;

    public Stream<Buchung> doImport() {
        Konto self = getKonto();
        return null;
//        return getUmsLines(self).map(new UmsLineUmsatzCaster().apply(self));
    }

    public Konto getKonto() {
        HBCIHandler handle = null;
//        HBCIPassport passport = HBCIPassportFactory.getPassport();
//        Konto[] konten = passport.getAccounts();
//        Konto k = konten[3];
        return null;
    }

    public Stream<GVRKUms.UmsLine> getUmsLines(Konto self) {
        HBCIHandler handle = null;
//        HBCIPassport passport = HBCIPassportFactory.getPassport();
        try {
            handle = new HBCIHandler(VERSION.getId(), null);
            HBCIJob umsatzJob = handle.newJob("KUmsAll");
            umsatzJob.setParam("my", self); // festlegen, welches Konto abgefragt werden soll.
            /*umsatzJob.addToQueue(); // Zur Liste der auszufuehrenden Auftraege hinzufuegen
            HBCIExecStatus status = handle.execute();
            if (!status.isOK()) {
                System.err.println(status.toString());
                return Stream.empty();
            }
            GVRKUms result = (GVRKUms) umsatzJob.getJobResult();
            if (!result.isOK()) {
                System.err.println(result.toString());
                return Stream.empty();
            }*/
//            return result.getFlatData().stream();
          return null;
        } finally {
            if (handle != null)
                handle.close();

            /*if (passport != null)
                passport.close();*/
        }
    }
}
