package de.danielr1996.banking.infrastructure.fints;

import java.io.File;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.domain.entities.Transaktionspartner;
import de.danielr1996.banking.domain.services.BuchungAbrufService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kapott.hbci.GV.HBCIJob;
import org.kapott.hbci.GV_Result.GVRKUms;
import org.kapott.hbci.exceptions.HBCI_Exception;
import org.kapott.hbci.manager.BankInfo;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.manager.HBCIUtils;
import org.kapott.hbci.manager.HBCIVersion;
import org.kapott.hbci.passport.AbstractHBCIPassport;
import org.kapott.hbci.passport.HBCIPassport;
import org.kapott.hbci.status.HBCIExecStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;


@Service
@Primary
@Slf4j
@Profile("fints-prod")
public class FinTSUmsatzAbrufService extends AbstractFinTSAbrufService implements BuchungAbrufService {
  private static final String JOB_NAME = "KUmsAllCamt";

  // FIXME: Remove rpcId
  private UmsatzAbrufResponse getUmsaetze(Konto konto, String rpcId) {
    HBCIPassport passport = this.getPassport(konto, rpcId);
    HBCIHandler handle = new HBCIHandler(VERSION.getId(), passport);
    org.kapott.hbci.structures.Konto k = chooseKonto(konto, passport);

    try {
      GVRKUms ums = getResult(handle, JOB_NAME, k);
      List<GVRKUms.UmsLine> umsaetze = ums.getFlatData();
      return UmsatzAbrufResponse.builder()
        .umsaetze(umsaetze)
        .konto(k)
        .build();
    } catch (HBCI_Exception e) {
      e.printStackTrace();
      return UmsatzAbrufResponse.builder()
        .umsaetze(new ArrayList<>())
        .konto(k)
        .build();
    } finally {
      handle.close();
      passport.close();
    }
  }

  // FIXME: Remove rpcId
  @Override
  public Stream<Buchung> getBuchungen(Konto konto, String rpcId) {
    UmsatzAbrufResponse res = getUmsaetze(konto, rpcId);
    org.kapott.hbci.structures.Konto self = res.getKonto();

    return res.getUmsaetze().stream().map(umsLine -> {
      Transaktionspartner otherPartner = null;
      if (umsLine.other.iban != null) {
        otherPartner = Transaktionspartner.builder()
          .bic(umsLine.other.bic)
          .blz(umsLine.other.blz)
          .iban(umsLine.other.iban)
          .name(umsLine.other.name)
          .build();
      }
      return Buchung.builder()
        .id(umsLine.id)
        .buchungstag(umsLine.bdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
        .valutadatum(umsLine.valuta.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
        .waehrung(umsLine.value.getCurr())
        .buchungstext(umsLine.text)
        .verwendungszweck(String.join("", umsLine.usage))
        .betrag(umsLine.value.getBigDecimalValue())
        .otherPartner(otherPartner)
        .selfPartner(Transaktionspartner.builder()
          .iban(self.iban)
          .bic(self.bic)
          .name(self.name)
          .blz(self.blz)
          .build())
        .build();
    });
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  private static class UmsatzAbrufResponse {
    private List<GVRKUms.UmsLine> umsaetze;
    private org.kapott.hbci.structures.Konto konto;
  }
}
