package de.danielr1996.banking.infrastructure.tasks;

import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.infrastructure.fints.HbciFintsCamtImporter;
import de.danielr1996.banking.infrastructure.fints.SaldoImporter;
import de.danielr1996.banking.domain.repository.BuchungRepository;
import de.danielr1996.banking.domain.repository.SaldoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@PropertySource("classpath:application.yml")
public class ImportTask {
  private static final int INTERVAL_IN_MINUTES = 100;

  @Autowired
  private BuchungRepository buchungRepository;

  @Autowired
  private SaldoRepository saldoRepository;

  private HbciFintsCamtImporter importer = new HbciFintsCamtImporter();
  private SaldoImporter saldoImporter = new SaldoImporter();

  @Scheduled(fixedRate = 60000 * INTERVAL_IN_MINUTES)
  public void reportCurrentTime() {
    List<Buchung> buchungen = importer.doImport().collect(Collectors.toList());
    Saldo saldo = saldoImporter.doImport();
    // FIXME: INSERT IF NOT EXISTS auf Datenbankebene
    buchungen.forEach(buchung -> {
//      if (!buchungRepository.existsById(buchung.getId())) {
      buchung.setOwnerId(UUID.fromString("360c5a1c-d95a-47fe-9b25-5416a504da3f"));
      buchungRepository.save(buchung);
//      }else{
//        buchungRepository.
//      }
    });
    saldoRepository.save(saldo);
    log.info("Saved {} Buchungen to Database", buchungen.size());
  }
}
