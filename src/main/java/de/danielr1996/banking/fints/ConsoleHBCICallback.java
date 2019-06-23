package de.danielr1996.banking.fints;

import de.danielr1996.banking.Config;
import org.kapott.hbci.callback.AbstractHBCICallback;
import org.kapott.hbci.exceptions.HBCI_Exception;
import org.kapott.hbci.passport.HBCIPassport;

import java.io.*;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

public class ConsoleHBCICallback extends AbstractHBCICallback {
    public final static String BLZ = Config.HBCI_BLZ;
    private final static String USER = Config.HBCI_USER;
    private static String PIN = "";

    public ConsoleHBCICallback() {


        PIN = loadParams();
        if (PIN == null) {
            Scanner s = new Scanner(System.in);
            System.out.println("Bitte geben Sie Ihren PIN/Passwort ein!");
            PIN = s.next();
            saveParamChanges(PIN);
        }
    }

    public String loadParams() {
        Properties props = new Properties();
        InputStream is = null;

        // First try loading from the current directory
        try {
            File f = new File("user.properties");
            is = new FileInputStream(f);
        } catch (Exception e) {
            is = null;
        }

        try {
            if (is == null) {
                // Try loading from classpath
                is = getClass().getResourceAsStream("user.properties");
            }

            // Try loading properties from the file (if found)
            props.load(is);
        } catch (Exception e) {
        }

        return props.getProperty("Password");
    }

    public void saveParamChanges(String password) {
        try {
            Properties props = new Properties();
            props.setProperty("Password", password);
            File f = new File("user.properties");
            OutputStream out = new FileOutputStream(f);
            props.store(out, "This is an optional header comment string");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @see org.kapott.hbci.callback.HBCICallback#log(java.lang.String, int, java.util.Date, java.lang.StackTraceElement)
     */
    @Override
    public void log(String msg, int level, Date date, StackTraceElement trace) {
        // Ausgabe von Log-Meldungen bei Bedarf
        // System.out.println(msg);
    }

    /**
     * @see org.kapott.hbci.callback.HBCICallback#callback(org.kapott.hbci.passport.HBCIPassport, int, java.lang.String, int, java.lang.StringBuffer)
     */
    @Override
    public void callback(HBCIPassport passport, int reason, String msg, int datatype, StringBuffer retData) {
        // Diese Funktion ist wichtig. Ueber die fragt HBCI4Java die benoetigten Daten von uns ab.
        switch (reason) {
            // Mit dem Passwort verschluesselt HBCI4Java die Passport-Datei.
            // Wir nehmen hier der Einfachheit halber direkt die PIN. In der Praxis
            // sollte hier aber ein staerkeres Passwort genutzt werden.
            // Die Ergebnis-Daten muessen in dem StringBuffer "retData" platziert werden.
            case NEED_PASSPHRASE_LOAD:
            case NEED_PASSPHRASE_SAVE:
                retData.replace(0, retData.length(), PIN);
                break;

            // PIN wird benoetigt
            case NEED_PT_PIN:
                retData.replace(0, retData.length(), PIN);
                break;

            // BLZ wird benoetigt
            case NEED_BLZ:
                retData.replace(0, retData.length(), BLZ);
                break;

            // Die Benutzerkennung
            case NEED_USERID:
                retData.replace(0, retData.length(), USER);
                break;

            // Die Kundenkennung. Meist identisch mit der Benutzerkennung.
            // Bei manchen Banken kann man die auch leer lassen
            case NEED_CUSTOMERID:
                retData.replace(0, retData.length(), USER);
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
                // Die Klasse "MatrixCode" kann zum Parsen der Daten verwendet werden
                try {
                    // MatrixCode code = new MatrixCode(retData.toString());

                    // Liefert den Mime-Type der grafik (i.d.R. "image/png").
                    // String type = code.getMimetype();

                    // Der Stream enthaelt jetzt die Binaer-Daten des Bildes
                    // InputStream stream = new ByteArrayInputStream(code.getImage());

                    // .... Hier Dialog mit der Grafik anzeigen und User-Eingabe der TAN
                    // Die Variable "msg" aus der Methoden-Signatur enthaelt uebrigens
                    // den bankspezifischen Text mit den Instruktionen fuer den User.
                    // Der Text aus "msg" sollte daher im Dialog dem User angezeigt
                    // werden.
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
                String code = "";
                retData.replace(0, retData.length(), code);
                break;

            // HBCI4Java benoetigt die TAN per smsTAN/chipTAN/weiteren TAN-Verfahren
            case NEED_PT_TAN:

                // Wenn per "retData" Daten uebergeben wurden, dann enthalten diese
                // den fuer chipTAN optisch zu verwendenden Flickercode.
                // Falls nicht, ist es eine TAN-Abfrage, fuer die keine weiteren
                // Parameter benoetigt werden (z.Bsp. beim smsTAN-Verfahren)

                // Die Variable "msg" aus der Methoden-Signatur enthaelt uebrigens
                // den bankspezifischen Text mit den Instruktionen fuer den User.
                // Der Text aus "msg" sollte daher im Dialog dem User angezeigt
                // werden.

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
                    String tan = null;
                    retData.replace(0, retData.length(), tan);
                }

                break;

            // Beim Verfahren smsTAN ist es moeglich, mehrere Handynummern mit
            // Aliasnamen bei der Bank zu hinterlegen. Auch wenn nur eine Handy-
            // Nummer bei der Bank hinterlegt ist, kann es durchaus passieren,
            // dass die Bank dennoch die Aufforderung zur Auswahl des TAN-Mediums
            // sendet.
            case NEED_PT_TANMEDIA:

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
                String alias = null;
                retData.replace(0, retData.length(), alias);

                break;
            //
            ////////////////////////////////////////////////////////////////////////

            // Manche Fehlermeldungen werden hier ausgegeben
            case HAVE_ERROR:
                System.out.println(msg);
                break;

            default:
                // Wir brauchen nicht alle der Callbacks
                break;

        }
    }

    /**
     * @see org.kapott.hbci.callback.HBCICallback#status(org.kapott.hbci.passport.HBCIPassport, int, java.lang.Object[])
     */
    @Override
    public void status(HBCIPassport passport, int statusTag, Object[] o) {
        // So aehnlich wie log(String,int,Date,StackTraceElement) jedoch fuer Status-Meldungen.
    }

}