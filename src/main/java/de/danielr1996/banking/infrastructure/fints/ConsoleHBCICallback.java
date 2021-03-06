package de.danielr1996.banking.infrastructure.fints;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.*;

import de.danielr1996.banking.application.auth.Password;
import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.infrastructure.security.PasswordDecrypter;
import lombok.extern.slf4j.Slf4j;
import org.kapott.hbci.callback.AbstractHBCICallback;
import org.kapott.hbci.callback.HBCICallback;
import org.kapott.hbci.exceptions.HBCI_Exception;
import org.kapott.hbci.manager.MatrixCode;
import org.kapott.hbci.passport.HBCIPassport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static de.danielr1996.banking.infrastructure.security.SecretKeyProvider.PASSPORT_SECRET;

/**
 * Implementation of the {@link AbstractHBCICallback} that reads callback values from the Console.
 */
@Slf4j
public class ConsoleHBCICallback extends AbstractHBCICallback {
  private String blz;
  private String user;
  private String pin;
  private String tanMedium;
  private String passportSecret;

  public ConsoleHBCICallback(Konto konto, PasswordDecrypter passwordDecrypter, String passportSecret) {
    this.blz = konto.getBlz();
    this.pin = passwordDecrypter.decrypt(konto.getPasswordhash());
    this.user = konto.getBankaccount();
    this.tanMedium = konto.getTanmedia();
    this.passportSecret = passportSecret;
  }

  public void callback(HBCIPassport passport, int reason, String msg, int datatype, StringBuffer retData) {
    switch (reason) {
      case NEED_PASSPHRASE_LOAD:
      case NEED_PASSPHRASE_SAVE:
        retData.replace(0, retData.length(), passportSecret);
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
          log.info("Bitte gebe die Tan ein:");
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
        log.info("Bitte gebe den Sicherheitsmechanismus ein:");
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
          log.info("Bitte gebe die Tan ein:");
          String tan = new Scanner(System.in).nextLine();
          retData.replace(0, retData.length(), tan);
        }
        break;
      case NEED_PT_TANMEDIA:
        if(tanMedium == null){
          log.info("Bitte gebe die Tan ein:");
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
    log.debug("{}", o);
  }

  @Override
  public void log(String msg, int level, Date date, StackTraceElement trace) {
    log.info(msg);
  }

  @Service
  @Profile("hbcicallback-console")
  public static class ConsoleHBCICallbackFactory implements HBCICallbackFactory {
    @Autowired
    private PasswordDecrypter passwordDecrypter;

    @Autowired
    @Qualifier(PASSPORT_SECRET)
    String secret;

    @Override
    public HBCICallback getCallBack(Konto konto, String rpcId) {
      return new ConsoleHBCICallback(konto, passwordDecrypter, secret);
    }
  }
}
