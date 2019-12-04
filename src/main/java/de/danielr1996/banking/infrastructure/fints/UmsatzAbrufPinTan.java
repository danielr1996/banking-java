package de.danielr1996.banking.infrastructure.fints;

import lombok.extern.slf4j.Slf4j;
import org.kapott.hbci.GV.HBCIJob;
import org.kapott.hbci.GV_Result.GVRKUms;
import org.kapott.hbci.GV_Result.GVRKUms.UmsLine;
import org.kapott.hbci.GV_Result.GVRSaldoReq;
import org.kapott.hbci.manager.BankInfo;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.manager.HBCIUtils;
import org.kapott.hbci.manager.HBCIVersion;
import org.kapott.hbci.passport.AbstractHBCIPassport;
import org.kapott.hbci.passport.HBCIPassport;
import org.kapott.hbci.status.HBCIExecStatus;
import org.kapott.hbci.structures.Konto;
import org.kapott.hbci.structures.Value;

import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 * Demo zum Abruf von Umsaetzen per PIN/TAN-Verfahren.
 * <p>
 * Die folgende Demo zeigt mit dem minimal noetigen Code, wie eine Umsatz-Abfrage
 * fuer ein Konto durchgefuehrt werden kann. Hierzu wird der Einfachheit halber
 * das Verfahren PIN/TAN verwendet, da es von den meisten Banken unterstuetzt wird.
 * <p>
 * Trage vor dem Ausfuehren des Programms die Zugangsdaten zu deinem Konto ein.
 */
@Slf4j
public class UmsatzAbrufPinTan {
  private final static HBCIVersion VERSION = HBCIVersion.HBCI_300;

  public static void main(String[] args) throws Exception {
    // HBCI4Java initialisieren
    // In "props" koennen optional Kernel-Parameter abgelegt werden, die in der Klasse
    // org.kapott.hbci.manager.HBCIUtils (oben im Javadoc) beschrieben sind.
    Properties props = new Properties();
    HBCIUtils.init(props, new ConsoleHBCICallback("76050101","518222","Reich", null, null));

    // In der Passport-Datei speichert HBCI4Java die Daten des Bankzugangs (Bankparameterdaten, Benutzer-Parameter, etc.).
    // Die Datei kann problemlos geloescht werden. Sie wird beim naechsten mal automatisch neu erzeugt,
    // wenn der Parameter "client.passport.PinTan.init" den Wert "1" hat (siehe unten).
    // Wir speichern die Datei der Einfachheit halber im aktuellen Verzeichnis.
    final File passportFile = new File("testpassport.dat");

    // Wir setzen die Kernel-Parameter zur Laufzeit. Wir koennten sie alternativ
    // auch oben in "props" setzen.
    HBCIUtils.setParam("client.passport.default", "PinTan"); // Legt als Verfahren PIN/TAN fest.
    HBCIUtils.setParam("client.passport.PinTan.init", "1"); // Stellt sicher, dass der Passport initialisiert wird

    // Erzeugen des Passport-Objektes.
    HBCIPassport passport = AbstractHBCIPassport.getInstance(passportFile);

    // Konfigurieren des Passport-Objektes.
    // Das kann alternativ auch alles ueber den Callback unten geschehen

    // Das Land.
    passport.setCountry("DE");

    // Server-Adresse angeben. Koennen wir entweder manuell eintragen oder direkt von HBCI4Java ermitteln lassen
    BankInfo info = HBCIUtils.getBankInfo("76050101");
    passport.setHost(info.getPinTanAddress());

    // TCP-Port des Servers. Bei PIN/TAN immer 443, da das ja ueber HTTPS laeuft.
    passport.setPort(443);

    // Art der Nachrichten-Codierung. Bei Chipkarte/Schluesseldatei wird
    // "None" verwendet. Bei PIN/TAN kommt "Base64" zum Einsatz.
    passport.setFilterType("Base64");

    // Das Handle ist die eigentliche HBCI-Verbindung zum Server
    HBCIHandler handle = null;

    try {
      // Verbindung zum Server aufbauen
      handle = new HBCIHandler(VERSION.getId(), passport);

      // Wir verwenden einfach das erste Konto, welches wir zur Benutzerkennung finden
      Konto[] konten = passport.getAccounts();
      if (konten == null || konten.length == 0)
        log.error("Keine Konten ermittelbar");

      log.info("Anzahl Konten: {}", konten.length);
      Konto k = konten[3];

      // 1. Auftrag fuer das Abrufen des Saldos erzeugen
      HBCIJob saldoJob = handle.newJob("SaldoReq");
      saldoJob.setParam("my", k); // festlegen, welches Konto abgefragt werden soll.
      saldoJob.addToQueue(); // Zur Liste der auszufuehrenden Auftraege hinzufuegen

      // 2. Auftrag fuer das Abrufen der Umsaetze erzeugen
      HBCIJob umsatzJob = handle.newJob("KUmsAll");
      umsatzJob.setParam("my", k); // festlegen, welches Konto abgefragt werden soll.
      umsatzJob.addToQueue(); // Zur Liste der auszufuehrenden Auftraege hinzufuegen

      // Hier koennen jetzt noch weitere Auftraege fuer diesen Bankzugang hinzugefuegt
      // werden. Z.Bsp. Ueberweisungen.

      // Alle Auftraege aus der Liste ausfuehren.
      HBCIExecStatus status = handle.execute();

      // Pruefen, ob die Kommunikation mit der Bank grundsaetzlich geklappt hat
      if (!status.isOK())
        log.error("{}",status);

      // Auswertung des Saldo-Abrufs.
      GVRSaldoReq saldoResult = (GVRSaldoReq) saldoJob.getJobResult();
      if (!saldoResult.isOK())
        log.error("{}",saldoResult);

      Value s = saldoResult.getEntries()[0].ready.value;
      log.info("Saldo: {}",s);


      // Das Ergebnis des Jobs koennen wir auf "GVRKUms" casten. Jobs des Typs "KUmsAll"
      // liefern immer diesen Typ.
      GVRKUms result = (GVRKUms) umsatzJob.getJobResult();

      // Pruefen, ob der Abruf der Umsaetze geklappt hat
      if (!result.isOK())
        log.error("{}",result);

      // Alle Umsatzbuchungen ausgeben
      List<UmsLine> buchungen = result.getFlatData();
      for (UmsLine buchung : buchungen) {
        StringBuilder sb = new StringBuilder();
        sb.append(buchung.valuta);

        Value v = buchung.value;
        if (v != null) {
          sb.append(": ");
          sb.append(v);
        }

        List<String> zweck = buchung.usage;
        if (zweck != null && zweck.size() > 0) {
          sb.append(" - ");
          // Die erste Zeile des Verwendungszwecks ausgeben
          sb.append(zweck.get(0));
        }

        // Ausgeben der Umsatz-Zeile
        log.info("{}",sb);
      }
    } finally {
      // Sicherstellen, dass sowohl Passport als auch Handle nach Beendigung geschlossen werden.
      if (handle != null)
        handle.close();

      if (passport != null)
        passport.close();
    }

  }
}
