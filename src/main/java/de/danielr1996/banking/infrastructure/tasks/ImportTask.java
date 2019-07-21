package de.danielr1996.banking.infrastructure.tasks;

import de.danielr1996.banking.domain.Buchung;
import de.danielr1996.banking.fints.mt940.HbciFintsCamtImporter;
import de.danielr1996.banking.repository.BuchungRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@PropertySource("classpath:application.yml")
public class ImportTask {
  private static final int INTERVAL_IN_MINUTES = 1;

  @Autowired
  private BuchungRepository buchungRepository;

  private HbciFintsCamtImporter importer = new HbciFintsCamtImporter();

  @Scheduled(fixedRate = 60000 * INTERVAL_IN_MINUTES)
  public void reportCurrentTime() {
    List<Buchung> buchungen = importer.doImport().collect(Collectors.toList());
    buchungRepository.saveAll(buchungen);
    log.info("Saved {} Buchungen to Database", buchungen.size());
  }
}
