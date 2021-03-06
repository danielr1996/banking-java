package de.danielr1996.banking.infrastructure.tasks;

import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.domain.repository.BuchungRepository;
import de.danielr1996.banking.domain.repository.KontoRepository;
import de.danielr1996.banking.domain.repository.SaldoRepository;
import de.danielr1996.banking.domain.repository.UserRepository;
import de.danielr1996.banking.domain.services.BuchungAbrufService;
import de.danielr1996.banking.domain.services.SaldoAbrufService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@PropertySource("classpath:config/application.yml")
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

  // FIXME: Remove rpcId
  @Scheduled(fixedRate = 60000 * INTERVAL_IN_MINUTES)
  public void importIntoDb(String username, String rpcId) {
    kontoRepository.findByUserId(username)
      .forEach(konto -> {
        Saldo saldo = saldoAbrufService.getSaldo(konto, rpcId);
        saldoRepository.save(saldo);
        List<Buchung> buchungen = buchungAbrufService.getBuchungen(konto, rpcId).collect(Collectors.toList());
        buchungen.forEach(buchung -> {
          buchung.setKontoId(konto.getId());
          buchungRepository.save(buchung);
        });
//        log.info("{} Buchungen importiert", buchungRepository.findAll().size());
      });
  }
}
