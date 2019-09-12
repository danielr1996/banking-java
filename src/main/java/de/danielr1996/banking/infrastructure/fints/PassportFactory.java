package de.danielr1996.banking.infrastructure.fints;

import org.kapott.hbci.manager.BankInfo;
import org.kapott.hbci.manager.HBCIUtils;
import org.kapott.hbci.passport.AbstractHBCIPassport;
import org.kapott.hbci.passport.HBCIPassport;

import java.io.File;
import java.util.Properties;

import static de.danielr1996.banking.infrastructure.fints.HBCICallback.BLZ;

public class PassportFactory {
  public static HBCIPassport getPassport(){
    // HBCI4Java initialisieren
    // In "props" koennen optional Kernel-Parameter abgelegt werden, die in der Klasse
    // org.kapott.hbci.manager.HBCIUtils (oben im Javadoc) beschrieben sind.
    Properties props = new Properties();
    HBCIUtils.init(props, new UmsatzAbrufPinTan.MyHBCICallback());

    // In der Passport-Datei speichert HBCI4Java die Daten des Bankzugangs (Bankparameterdaten, Benutzer-Parameter, etc.).
    // Die Datei kann problemlos geloescht werden. Sie wird beim naechsten mal automatisch neu erzeugt,
    // wenn der Parameter "client.passport.PinTan.init" den Wert "1" hat (siehe unten).
    // Wir speichern die Datei der Einfachheit halber im aktuellen Verzeichnis.
    final File passportFile = new File("passport.dat");

    // Wir setzen die Kernel-Parameter zur Laufzeit. Wir koennten sie alternativ
    // auch oben in "props" setzen.
    HBCIUtils.setParam("client.passport.default", "PinTan"); // Legt als Verfahren PIN/TAN fest.
    HBCIUtils.setParam("client.passport.PinTan.filename", passportFile.getAbsolutePath());
    HBCIUtils.setParam("client.passport.PinTan.init", "1");

    // Erzeugen des Passport-Objektes.
    HBCIPassport passport = AbstractHBCIPassport.getInstance();

    // Konfigurieren des Passport-Objektes.
    // Das kann alternativ auch alles ueber den Callback unten geschehen

    // Das Land.
    passport.setCountry("DE");

    // Server-Adresse angeben. Koennen wir entweder manuell eintragen oder direkt von HBCI4Java ermitteln lassen
    BankInfo info = HBCIUtils.getBankInfo(BLZ);
    passport.setHost(info.getPinTanAddress());

    // TCP-Port des Servers. Bei PIN/TAN immer 443, da das ja ueber HTTPS laeuft.
    passport.setPort(443);

    // Art der Nachrichten-Codierung. Bei Chipkarte/Schluesseldatei wird
    // "None" verwendet. Bei PIN/TAN kommt "Base64" zum Einsatz.
    passport.setFilterType("Base64");
//    System.out.println(passport.getAccounts().length);
    return passport;
  }
}
