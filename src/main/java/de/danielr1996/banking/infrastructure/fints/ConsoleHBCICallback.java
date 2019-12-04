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
public class ConsoleHBCICallback extends AbstractHBCICallback {
  private String blz;
  private String user;
  private String pin;
  private final static String PASSPORT_PIN = "PASSPORTPIN";
  private String tanMedium;

  public ConsoleHBCICallback(String blz, String user, String pin) {
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
        try {
          MatrixCode code = new MatrixCode(retData.toString());
          String type = code.getMimetype();
          InputStream stream = new ByteArrayInputStream(code.getImage());
          byte[] buffer = new byte[stream.available()];
          stream.read(buffer);
          File targetFile = new File("C:\\Users\\Dani\\Desktop\\tan.png");
          targetFile.createNewFile();
          OutputStream outStream = new FileOutputStream(targetFile);
          outStream.write(buffer);
          BufferedImage img = ImageIO.read(new File("C:\\Users\\Dani\\Desktop\\tan.png"));
          ImageIcon icon = new ImageIcon(img);
          JFrame frame = new JFrame();
          frame.setLayout(new FlowLayout());
          frame.setSize(200, 300);
          JLabel lbl = new JLabel();
          lbl.setIcon(icon);
          frame.add(lbl);
          frame.setVisible(true);
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        try {
          String tan = null;
          retData.replace(0, retData.length(), tan);
        } catch (Exception e) {
          throw new HBCI_Exception(e);
        }
        break;
      case NEED_PT_SECMECH:
        log.info("[NEED_PT_SECMECH]: {} - {}", msg, retData);
        String secmech = new Scanner(System.in).nextLine();
        retData.replace(0, retData.length(), secmech);
        break;
      case NEED_PT_TAN:
        log.info("[NEED_PT_TAN]: {} - {}", msg, retData);
        String flicker = retData.toString();
        if (flicker != null && flicker.length() > 0) {
          String tan = null;
          retData.replace(0, retData.length(), tan);
        } else {
          String tan = new Scanner(System.in).nextLine();
          retData.replace(0, retData.length(), tan);
        }
        break;
      case NEED_PT_TANMEDIA:
        if (tanMedium == null) {
          tanMedium = new Scanner(System.in).nextLine();
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

  @Override
  public void status(HBCIPassport passport, int statusTag, Object[] o) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void log(String msg, int level, Date date, StackTraceElement trace) {
    throw new UnsupportedOperationException();
  }
}
