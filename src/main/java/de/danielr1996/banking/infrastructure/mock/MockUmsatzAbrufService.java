package de.danielr1996.banking.infrastructure.mock;

import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.domain.entities.Buchung;
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
    HBCICallback hbciCallback = hbciCallbackFactory.getCallBack(konto.getBlz(), konto.getKontonummer(), konto.getPasswordhash(), rpcId);

    StringBuffer tan = new StringBuffer();
    hbciCallback.callback(null, HBCICallback.NEED_PT_TAN, "MockUmsatzAbrufService benötigt TAN", 0, tan);
    log.info("MockUmsatzAbrufService hat TAN erhalten: {}", tan);

    return Stream.of(
      Buchung.builder()
        .id(UUID.randomUUID().toString())
        .betrag(BigDecimal.valueOf(-100))
        .kontoId(konto.getId())
        .build(),
      Buchung.builder()
        .id(UUID.randomUUID().toString())
        .betrag(BigDecimal.valueOf(200))
        .kontoId(konto.getId())
        .build()
    );
  }
}
