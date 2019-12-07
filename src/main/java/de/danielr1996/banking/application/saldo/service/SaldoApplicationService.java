package de.danielr1996.banking.application.saldo.service;

import de.danielr1996.banking.application.saldo.dto.SaldiContainer;
import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.domain.repository.BuchungRepository;
import de.danielr1996.banking.domain.repository.SaldoRepository;
import de.danielr1996.banking.domain.services.AggregateSaldoDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

  public SaldiContainer getSaldiContainer(List<UUID> kontoIds, int page, int size) {
    // FIXME: In Domain/Aplication Service auslagern
    List<Buchung> buchungen = buchungRepository.findByKontoIdIn(kontoIds);
    Saldo lastSaldo = kontoIds.stream()
      .map(saldoRepository::findByKontoId)
      .reduce(Saldo::add)
      .orElseGet(Saldo::new);
    List<Saldo> aggregated = AggregateSaldoDomainService.aggregateSaldi(buchungen, lastSaldo);

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

  public Saldo getSaldo(List<UUID> kontoIds) {
    return kontoIds.stream()
      .map(kontoId -> saldoRepository.findByKontoId(kontoId))
      .reduce(Saldo::add)
      .orElse(Saldo.builder().build());
  }
}
