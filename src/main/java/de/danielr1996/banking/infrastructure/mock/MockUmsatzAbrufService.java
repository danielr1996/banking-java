package de.danielr1996.banking.infrastructure.mock;

import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.entities.Transaktionspartner;
import de.danielr1996.banking.domain.services.BuchungAbrufService;
import de.danielr1996.banking.infrastructure.fints.HBCICallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.kapott.hbci.callback.HBCICallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
@Profile("fints-mock")
@Slf4j
public class MockUmsatzAbrufService implements BuchungAbrufService {

  private HBCICallbackFactory hbciCallbackFactory;

  @Autowired
  private MockUmsatzAbrufService(HBCICallbackFactory hbciCallbackFactory) {
    this.hbciCallbackFactory = hbciCallbackFactory;
  }

  // TODO: Remove rpcId
  @Override
  public Stream<Buchung> getBuchungen(Konto konto, String rpcId) {
    HBCICallback hbciCallback = hbciCallbackFactory.getCallBack(konto, rpcId);

    StringBuffer tan = new StringBuffer();
    hbciCallback.callback(null, HBCICallback.NEED_PT_TAN, "MockUmsatzAbrufService benötigt TAN", 0, tan);
    log.info("MockUmsatzAbrufService hat TAN erhalten: {}", tan);

    return Stream.of(
      Buchung.builder()
        .selfPartner(getSelf())
        .otherPartner(getPaypal())
        .valutadatum(LocalDateTime.now())
        .buchungstag(LocalDateTime.now())
        .buchungstext("Überweisung")
        .verwendungszweck("Überweisung")
        .waehrung("EUR")
        .id(UUID.randomUUID().toString())
        .betrag(BigDecimal.valueOf(-100))
        .kontoId(konto.getId())
        .build()
    );
  }

  Transaktionspartner getSelf(){
    return Transaktionspartner.builder()
      .iban("DE59500105171949296971")
      .bic("SSKNDE7XXX")
      .name("Daniel Richter")
      .blz("50010517")
      .build();
  }

  Transaktionspartner getPaypal(){
    return Transaktionspartner.builder()
      .iban("DE50500105171282378289")
      .bic("SSKNDE7XXX")
      .name("PayPal")
      .blz("50010517")
      .build();
  }
}
