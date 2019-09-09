package de.danielr1996.banking.infrastructure.fints;

import lombok.extern.slf4j.Slf4j;
import org.kapott.hbci.callback.AbstractHBCICallback;
import org.kapott.hbci.passport.HBCIPassport;

import java.util.Date;

@Slf4j
public class HBCICallback extends AbstractHBCICallback {
  /**
   * Die BLZ deiner Bank.
   */
  public final static String BLZ = "76050101";

  /**
   * Deine Benutzerkennung.
   */
  private final static String USER = "518222";

  /**
   * Deine PIN.
   */
  private final static String PIN = "Reich";

  @Override
  public void log(String s, int i, Date date, StackTraceElement stackTraceElement) {
    log.warn(s);
  }

  @Override
  public void callback(HBCIPassport passport, int reason, String msg, int datatype, StringBuffer retData) {
    switch (reason) {
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
      case HAVE_ERROR:
        log.error(msg);
      default:
        break;
    }
  }

  @Override
  public void status(HBCIPassport hbciPassport, int i, Object[] objects) {
    log.info("{}", hbciPassport);
  }
}
