package de.danielr1996.banking.infrastructure.tasks;

import de.danielr1996.banking.domain.Buchung;
import de.danielr1996.banking.fints.mt940.HbciFintsMt940Importer;
import de.danielr1996.banking.repository.BuchungRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ImportTask {
    @Autowired
    private BuchungRepository buchungRepository;

    private HbciFintsMt940Importer importer = new HbciFintsMt940Importer();

    @Scheduled(fixedRate =  1 * 60 * 1000*10)
    public void reportCurrentTime(){
        List<Buchung> buchungen = importer.doImport().collect(Collectors.toList());
        buchungRepository.saveAll(buchungen);
        log.info("Saved {} Buchungen to Database", buchungen.size());
    }
}
