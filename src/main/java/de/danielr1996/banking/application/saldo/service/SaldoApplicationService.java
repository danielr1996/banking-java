package de.danielr1996.banking.application.saldo.service;

import de.danielr1996.banking.application.saldo.dto.SaldiContainer;
import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.domain.services.AggregateSaldoDomainService;
import de.danielr1996.banking.domain.repository.BuchungRepository;
import de.danielr1996.banking.domain.repository.SaldoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SaldoApplicationService {

  @Autowired
  private SaldoRepository saldoRepository;

  @Autowired
  private BuchungRepository buchungRepository;

  @Autowired
  private GetNewestSaldoService getNewestSaldoService;

  public SaldiContainer getSaldiContainer(UUID kontoId, int page, int size) {
    List<Buchung> buchungen = buchungRepository
      // FIXME: In Domain/Aplication Service auslagern
      .findAll(Example.of(Buchung.builder().kontoId(kontoId).build()));
    List<Saldo> saldi = saldoRepository.findAll();
    Saldo lastSaldo = getNewestSaldoService.getNewestSaldo(saldi, kontoId);

    List<Saldo> aggregated = AggregateSaldoDomainService.aggregateSaldi(kontoId, buchungen, lastSaldo);

    List<Saldo> filtered = aggregated
      .stream()
      .skip(page * size)
      .limit(size)
      .collect(Collectors.toList());
    return SaldiContainer.builder()
      .saldi(filtered)
      .totalPages((long) Math.ceil((double) aggregated.size() / size))
      .totalElements(aggregated.size())
      .build();
  }

  public Saldo getNewestSaldo(UUID kontoId) {
    List<Saldo> saldi = saldoRepository.findAll();
    return getNewestSaldoService.getNewestSaldo(saldi, kontoId);
  }
}
