package de.danielr1996.banking.infrastructure.fints;

import io.crossbar.autobahn.wamp.Client;
import io.crossbar.autobahn.wamp.Session;
import io.crossbar.autobahn.wamp.types.ExitInfo;
import io.crossbar.autobahn.wamp.types.SessionDetails;
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
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

@Slf4j
public class WampHBCICallback extends AbstractHBCICallback {
  private String blz;
  private String user;
  private String pin;
  private final static String PASSPORT_PIN = "PASSPORTPIN";
  private String rpcId;
  private String tanMedium;

  public WampHBCICallback(String blz, String user, String pin, String rpcId) {
    this.blz = blz;
    this.pin = pin;
    this.user = user;
    this.rpcId = rpcId;
  }

  /**
   * @see org.kapott.hbci.callback.HBCICallback#log(String, int, Date, StackTraceElement)
   */
  public void log(String msg, int level, Date date, StackTraceElement trace) {
    // Ausgabe von Log-Meldungen bei Bedarf
    log.info(msg);
  }

  /**
   * @see org.kapott.hbci.callback.HBCICallback#callback(HBCIPassport, int, String, int, StringBuffer)
   */
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
        try {
          MatrixCode code = new MatrixCode(retData.toString());
          String type = code.getMimetype();
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
          retData.replace(0, retData.length(), null);
        } catch (Exception e) {
          log.error("Fehler beim Anzeigen der PhotoTan");
          log.error("{}", e.getClass());
          throw new HBCI_Exception(e);
        }
        break;
      case NEED_PT_QRTAN:
        log.info("[NEED_PT_QRTAN]: {} - {}", msg, retData);
        retData.replace(0, retData.length(), null);
        break;
      case NEED_PT_SECMECH:
        log.info("[NEED_PT_SECMECH]: {} - {}", msg, retData);
        retData.replace(0, retData.length(), get("NEED_PT_SECMECH", rpcId));
        break;
      case NEED_PT_TAN:
        log.info("[NEED_PT_TAN]: {} - {}", msg, retData);
        String flicker = retData.toString();
        if (flicker != null && flicker.length() > 0) {
          retData.replace(0, retData.length(), null);
        } else {
          retData.replace(0, retData.length(), get("NEED_PT_TAN", rpcId));
        }
        break;
      case NEED_PT_TANMEDIA:
        log.info("[NEED_PT_TANMEDIA]: {} - {}", msg, retData);

        if (tanMedium == null) {
          tanMedium = get("NEED_PT_TANMEDIA", rpcId);
        }

        retData.replace(0, retData.length(), tanMedium);
        break;
      case HAVE_ERROR:
        log.error(msg);
        break;
      default:
        break;

    }
  }

  private String get(String methodname, String rpcId) {
    Session session = new Session();
    CountDownLatch latch = new CountDownLatch(1);
    session.addOnJoinListener((Session s, SessionDetails sd) -> latch.countDown());
    Client client = new Client(session, "ws://127.0.0.1:9090/wamp", "default");
    CompletableFuture<ExitInfo> connection = client.connect();

    List<Object> result = null;
    try {
      latch.await();
      String methodName = "de.danielr1996." + methodname + "." + rpcId;
      System.out.println(methodName);
      result = session.call(methodName, 11, 12).get().results;
      client.connect().get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    String tan = result.get(0).toString();
    return tan;
  }

  /**
   * @see org.kapott.hbci.callback.HBCICallback#status(HBCIPassport, int, Object[])
   */
  public void status(HBCIPassport passport, int statusTag, Object[] o) {
//    log.info("Status: {}, Objekt: {}", statusTag, o);
  }
}
