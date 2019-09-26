package de.danielr1996.banking.infrastructure.fints;

import lombok.extern.slf4j.Slf4j;
import org.kapott.hbci.callback.AbstractHBCICallback;
import org.kapott.hbci.exceptions.HBCI_Exception;
import org.kapott.hbci.manager.MatrixCode;
import org.kapott.hbci.passport.HBCIPassport;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;
import java.util.Scanner;

@Slf4j
public class MyHBCICallback extends AbstractHBCICallback {
  private String blz = "76050101";
  private String user = "518222";
  private String pin = "Reich";
  private final static String PASSPORT_PIN = "PASSPORTPIN";
  private String tanAlias = null;


  public MyHBCICallback(String blz, String user, String pin){
    this.blz = blz;
    this.pin = pin;
    this.user = user;
  }

  /**
   * @see org.kapott.hbci.callback.HBCICallback#log(java.lang.String, int, java.util.Date, java.lang.StackTraceElement)
   */
  public void log(String msg, int level, Date date, StackTraceElement trace) {
    // Ausgabe von Log-Meldungen bei Bedarf
    log.info(msg);
  }

  /**
   * @see org.kapott.hbci.callback.HBCICallback#callback(org.kapott.hbci.passport.HBCIPassport, int, java.lang.String, int, java.lang.StringBuffer)
   */
  public void callback(HBCIPassport passport, int reason, String msg, int datatype, StringBuffer retData) {
    // Diese Funktion ist wichtig. Ueber die fragt HBCI4Java die benoetigten Daten von uns ab.
    System.out.println(reason);
    switch (reason) {
      // Mit dem Passwort verschluesselt HBCI4Java die Passport-Datei.
      // Wir nehmen hier der Einfachheit halber direkt die PIN. In der Praxis
      // sollte hier aber ein staerkeres Passwort genutzt werden.
      // Die Ergebnis-Daten muessen in dem StringBuffer "retData" platziert werden.
      case NEED_PASSPHRASE_LOAD:
      case NEED_PASSPHRASE_SAVE:
        retData.replace(0, retData.length(), PASSPORT_PIN);
        break;
      case NEED_PT_PIN:
        retData.replace(0, retData.length(), pin);
        break;
      case NEED_BLZ:
        retData.replace(0, retData.length(), blz);
        break;
      case NEED_USERID:
      case NEED_CUSTOMERID:
        retData.replace(0, retData.length(), user);
        break;

      ////////////////////////////////////////////////////////////////////////
      // Die folgenden Callbacks sind nur fuer die Ausfuehrung TAN-pflichtiger
      // Geschaeftsvorfaelle bei der Verwendung des PIN/TAN-Verfahrens noetig.
      // Z.Bsp. beim Versand einer Ueberweisung
      // "NEED_PT_SECMECH" kann jedoch auch bereits vorher auftreten.

      // HBCI4Java benoetigt die TAN per PhotoTAN-Verfahren
      // Liefert die anzuzeigende PhotoTAN-Grafik, die mit der entsprechenden
      // Smartphone-App der Bank fotografiert werden muss, um die TAN
      // zu generieren. Eine Implementierung muss diese Grafik anzeigen
      // sowie ein Eingabefeld fuer die TAN. Der Callback muss dann die vom
      // User eingegebene TAN zurueckliefern (nachdem dieser die Grafik
      // fotografiert und die App ihm die TAN angezeigt hat)
      case NEED_PT_PHOTOTAN:
        log.info("[NEED_PT_PHOTOTAN]: {}", msg);
        // Die Klasse "MatrixCode" kann zum Parsen der Daten verwendet werden
        try {
          MatrixCode code = new MatrixCode(retData.toString());

          // Liefert den Mime-Type der grafik (i.d.R. "image/png").
          String type = code.getMimetype();

          // Der Stream enthaelt jetzt die Binaer-Daten des Bildes
          InputStream stream = new ByteArrayInputStream(code.getImage());
          byte[] buffer = new byte[stream.available()];
          stream.read(buffer);

          File targetFile = new File("C:\\Users\\Daniel\\Desktop\\tan.png");
          targetFile.createNewFile();
          OutputStream outStream = new FileOutputStream(targetFile);
          outStream.write(buffer);
          BufferedImage img = ImageIO.read(new File("C:\\Users\\Daniel\\Desktop\\tan.png"));
          ImageIcon icon = new ImageIcon(img);
          JFrame frame = new JFrame();
          frame.setLayout(new FlowLayout());
          frame.setSize(200, 300);
          JLabel lbl = new JLabel();
          lbl.setIcon(icon);
          frame.add(lbl);
          frame.setVisible(true);
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          // .... Hier Dialog mit der Grafik anzeigen und User-Eingabe der TAN
          // Die Variable "msg" aus der Methoden-Signatur enthaelt uebrigens
          // den bankspezifischen Text mit den Instruktionen fuer den User.
          // Der Text aus "msg" sollte daher im Dialog dem User angezeigt
          // werden.
          String tan = new Scanner(System.in).nextLine();
          retData.replace(0, retData.length(), tan);
        } catch (Exception e) {
          log.error("Fehler beim Anzeigen der PhotoTan");
          log.error("{}", e.getClass());
          throw new HBCI_Exception(e);
        }

        break;

      case NEED_PT_QRTAN:
        log.info("[NEED_PT_QRTAN]: {} - {}", msg, retData);
        // Die Klasse "QRCode" kann zum Parsen der Daten verwendet werden
        try {
          // QRCode code = new QRCode(retData.toString(),msg);

          // Der Stream enthaelt jetzt die Binaer-Daten des Bildes
          // InputStream stream = new ByteArrayInputStream(code.getImage());

          // .... Hier Dialog mit der Grafik anzeigen und User-Eingabe der TAN
          // Die Variable "msg" aus der Methoden-Signatur enthaelt uebrigens
          // den bankspezifischen Text mit den Instruktionen fuer den User.
          // Der Text aus "msg" sollte daher im Dialog dem User angezeigt
          // werden. Da Sparkassen den eigentlichen Bild u.U. auch in msg verpacken,
          // sollte zur Anzeige nicht der originale Text verwendet werden sondern
          // der von QRCode - dort ist dann die ggf. enthaltene Base64-codierte QR-Grafik entfernt
          // msg = code.getMessage();
          String tan = null;
          retData.replace(0, retData.length(), tan);
        } catch (Exception e) {
          throw new HBCI_Exception(e);
        }

        break;

      // HBCI4Java benoetigt den Code des verwendenden TAN-Verfahren (smsTAN,
      // chipTAN optisch, photoTAN,...)
      // I.d.R. ist das eine dreistellige mit "9" beginnende Ziffer
      case NEED_PT_SECMECH:
        log.info("[NEED_PT_SECMECH]: {} - {}", msg, retData);
        // Als Parameter werden die verfuegbaren TAN-Verfahren uebergeben.
        // Der Aufbau des String ist wie folgt:
        // <code1>:<name1>|<code2>:<name2>|...
        // Bsp:
        // 911:smsTAN|920:chipTAN optisch|955:photoTAN
        // String options = retData.toString();

        // Der Callback muss den Code des zu verwendenden TAN-Verfahrens
        // zurueckliefern
        // In "code" muss der 3-stellige Code des vom User gemaess obigen
        // Optionen ausgewaehlte Verfahren eingetragen werden
        String code = new Scanner(System.in).nextLine();
        retData.replace(0, retData.length(), code);
        break;

      // HBCI4Java benoetigt die TAN per smsTAN/chipTAN/weiteren TAN-Verfahren
      case NEED_PT_TAN:
        log.info("[NEED_PT_TAN]: {} - {}", msg, retData);

        // Wenn per "retData" Daten uebergeben wurden, dann enthalten diese
        // den fuer chipTAN optisch zu verwendenden Flickercode.
        // Falls nicht, ist es eine TAN-Abfrage, fuer die keine weiteren
        // Parameter benoetigt werden (z.Bsp. beim smsTAN-Verfahren)

        // Die Variable "msg" aus der Methoden-Signatur enthaelt uebrigens
        // den bankspezifischen Text mit den Instruktionen fuer den User.
        // Der Text aus "msg" sollte daher im Dialog dem User angezeigt
        // werden.
        log.info("Flicker: {}", retData);
        String flicker = retData.toString();
        if (flicker != null && flicker.length() > 0) {
          // Ist chipTAN optisch. Es muss ein animierter Barcode angezeigt
          // werden. Hierfuer kann die Hilfsklasse "FlickerRenderer" verwendet
          // werden. Diese enthalt bereits das Parsen. Es muss lediglich die
          // Methode "paint" ueberschrieben werden.
          // FlickerRenderer renderer = new FlickerRenderer(flicker);

          // Hier TAN-Abfrage mit dem animierten Barcode anzeigen sowie
          // Eingabefeld fuer die TAN
          String tan = null;
          retData.replace(0, retData.length(), tan);
        } else {
          // Ist smsTAN, iTAN, o.ae.
          // Dialog zur TAN-Eingabe anzeigen mit dem Text aus "msg".
          String tan = new Scanner(System.in).nextLine();
          System.out.println(tan);
          retData.replace(0, retData.length(), tan);
        }

        break;

      // Beim Verfahren smsTAN ist es moeglich, mehrere Handynummern mit
      // Aliasnamen bei der Bank zu hinterlegen. Auch wenn nur eine Handy-
      // Nummer bei der Bank hinterlegt ist, kann es durchaus passieren,
      // dass die Bank dennoch die Aufforderung zur Auswahl des TAN-Mediums
      // sendet.
      case NEED_PT_TANMEDIA:
        log.info("[NEED_PT_TANMEDIA]: {} - {}", msg, retData);
        // Als Parameter werden die verfuegbaren TAN-Medien uebergeben.
        // Der Aufbau des String ist wie folgt:
        // <name1>|<name2>|...
        // Bsp:
        // Privathandy|Firmenhandy
        // String options = retData.toString();

        // Der Callback muss den vom User ausgewaehlten Aliasnamen
        // zurueckliefern. Falls "options" kein "|" enthaelt, ist davon
        // auszugehen, dass nur eine moegliche Option existiert. In dem
        // Fall ist keine Auswahl noetig und "retData" kann unveraendert
        // bleiben
        if (tanAlias == null) {
          tanAlias = new Scanner(System.in).nextLine();
        }
        retData.replace(0, retData.length(), tanAlias);

        break;
      case HAVE_ERROR:
        log.error(msg);
        break;
      default:
        break;

    }
  }

  /**
   * @see org.kapott.hbci.callback.HBCICallback#status(org.kapott.hbci.passport.HBCIPassport, int, java.lang.Object[])
   */
  public void status(HBCIPassport passport, int statusTag, Object[] o) {
//    log.info("Status: {}, Objekt: {}", statusTag, o);
  }

}
