package de.danielr1996.banking.fints.mt940;

import de.danielr1996.banking.domain.Buchung;
import de.danielr1996.banking.domain.TransaktionsPartner;
import org.kapott.hbci.GV_Result.GVRKUms;
import org.kapott.hbci.structures.Konto;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Base64;
import java.util.UUID;
import java.util.function.Function;

public class UmsLineUmsatzCaster implements Function<Konto, Function<GVRKUms.UmsLine, Buchung>> {
  public Function<GVRKUms.UmsLine, Buchung> apply(Konto self) {
    return umsLine -> {
      return Buchung.builder()
        .id(umsLine.id)
        .buchungstag(umsLine.bdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
        .valutadatum(umsLine.valuta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
        .waehrung(umsLine.value.getCurr())
        .buchungstext(umsLine.text)
        .verwendungszweck(String.join("", umsLine.usage))
        .betrag(umsLine.value.getBigDecimalValue())
        .otherPartner(TransaktionsPartner.builder()
          .iban(umsLine.other.iban)
          .bic(umsLine.other.bic)
          .name(umsLine.other.name)
          .build())
        .selfPartner(TransaktionsPartner.builder()
          .iban(self.iban)
          .bic(self.bic)
          .name(self.name)
          .build())
        .build();
    };
  }
}
