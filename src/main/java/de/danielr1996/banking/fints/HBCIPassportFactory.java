package de.danielr1996.banking.fints;

import org.kapott.hbci.manager.BankInfo;
import org.kapott.hbci.manager.HBCIUtils;
import org.kapott.hbci.passport.AbstractHBCIPassport;
import org.kapott.hbci.passport.HBCIPassport;

import java.io.File;
import java.util.Properties;

public class HBCIPassportFactory {
    public static HBCIPassport getPassport(){
        Properties props = new Properties();
        HBCIUtils.init(props, new ConsoleHBCICallback());
        final File passportFile = new File("testpassport.dat");
        HBCIUtils.setParam("client.passport.default", "PinTan"); // Legt als Verfahren PIN/TAN fest.
        HBCIUtils.setParam("client.passport.PinTan.filename", passportFile.getAbsolutePath());
        HBCIUtils.setParam("client.passport.PinTan.init", "1");
        HBCIPassport passport = AbstractHBCIPassport.getInstance();
        passport.setCountry("DE");
        BankInfo info = HBCIUtils.getBankInfo(ConsoleHBCICallback.BLZ);
        passport.setHost(info.getPinTanAddress());
        passport.setPort(443);
        passport.setFilterType("Base64");
        return passport;
    }
}
