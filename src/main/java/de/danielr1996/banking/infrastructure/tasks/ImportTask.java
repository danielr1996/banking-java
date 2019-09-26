package de.danielr1996.banking.infrastructure.tasks;

import de.danielr1996.banking.auth.User;
import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.domain.repository.KontoRepository;
import de.danielr1996.banking.domain.repository.UserRepository;
import de.danielr1996.banking.domain.services.BuchungAbrufService;
import de.danielr1996.banking.domain.services.SaldoAbrufService;
import de.danielr1996.banking.infrastructure.fints.SaldoImporter;
import de.danielr1996.banking.domain.repository.BuchungRepository;
import de.danielr1996.banking.domain.repository.SaldoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@PropertySource("classpath:application.yml")
public class ImportTask {
  private static final int INTERVAL_IN_MINUTES = 100;

  @Autowired
  private BuchungRepository buchungRepository;

  @Autowired
  private SaldoRepository saldoRepository;

  @Autowired
  private BuchungAbrufService buchungAbrufService;

  @Autowired
  private SaldoAbrufService saldoAbrufService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private KontoRepository kontoRepository;


  @Scheduled(fixedRate = 60000 * INTERVAL_IN_MINUTES)
  public void reportCurrentTime() {
    kontoRepository.findAll().stream()
      .forEach(konto -> {
        List<Buchung> buchungen = buchungAbrufService.getBuchungen(konto).collect(Collectors.toList());
        buchungen.forEach(buchung -> {
          System.out.println(buchung);
          buchung.setOwnerId(konto.getUserId());
          buchungRepository.save(buchung);
        });
      });


    /*List<Buchung> buchungen = buchungAbrufService.getBuchungen(null).collect(Collectors.toList());
    Saldo saldo = saldoAbrufService.getSaldo(null);
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
    log.info("Saved {} Buchungen to Database", buchungen.size());*/
  }
}
