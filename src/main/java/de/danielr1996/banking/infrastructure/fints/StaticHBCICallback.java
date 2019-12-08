package de.danielr1996.banking.infrastructure.fints;

import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.kapott.hbci.callback.AbstractHBCICallback;
import org.kapott.hbci.callback.HBCICallback;
import org.kapott.hbci.passport.HBCIPassport;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link AbstractHBCICallback} that returns static values from the Callback. Can be Used for Testing
 */
@Slf4j
public class StaticHBCICallback extends AbstractHBCICallback {
  private String blz;
  private String user;
  private String pin;
  private final static String PASSPORT_PIN = "PASSPORTPIN";

  public StaticHBCICallback(String blz, String user, String pin) {
    this.blz = blz;
    this.pin = pin;
    this.user = user;
  }

  public void callback(HBCIPassport passport, int reason, String msg, int datatype, StringBuffer retData) {
    switch (reason) {
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
      case NEED_PT_PHOTOTAN:
        log.info("[NEED_PT_PHOTOTAN]: {}", msg);
        retData.replace(0, retData.length(), "TAN");
        break;
      case NEED_PT_QRTAN:
        log.info("[NEED_PT_QRTAN]: {} - {}", msg, retData);
        retData.replace(0, retData.length(), "TAN");
        break;
      case NEED_PT_SECMECH:
        log.info("[NEED_PT_SECMECH]: {} - {}", msg, retData);
        log.info("Bitte gebe den Sicherheitsmechanismus ein:");
        retData.replace(0, retData.length(), "SECMECH");
        break;
      case NEED_PT_TAN:
        log.info("[NEED_PT_TAN]: {} - {}", msg, retData);
        retData.replace(0, retData.length(), "TAN");
        break;
      case NEED_PT_TANMEDIA:
        retData.replace(0, retData.length(), "TANMEDIUM");
        break;
      case HAVE_ERROR:
        log.error(msg);
        break;
      default:
        break;

    }
  }

  @Override
  public void status(HBCIPassport passport, int statusTag, Object[] o) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void log(String msg, int level, Date date, StackTraceElement trace) {
    throw new UnsupportedOperationException();
  }

  @Service
  @Profile("hbcicallback-static")
  public static class StaticHBCICallbackFactory implements HBCICallbackFactory {

    @Override
    public HBCICallback getCallBack(String blz, String kontonummer, String passwordhash, String rpcId) {
      return new StaticHBCICallback(blz, kontonummer, passwordhash);
    }
  }
}
