package de.danielr1996.banking.application;

import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.domain.services.AggregateSaldoService;
import de.danielr1996.banking.infrastructure.graphql.SaldiContainer;
import de.danielr1996.banking.domain.repository.BuchungRepository;
import de.danielr1996.banking.domain.repository.SaldoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PageSaldoService {

  @Autowired
  private SaldoRepository saldoRepository;

  @Autowired
  private BuchungRepository buchungRepository;

  @Autowired
  private AggregateSaldoService aggregateSaldoService;

  @Autowired
  private GetNewestSaldoService getNewestSaldoService;

  public SaldiContainer getSaldiContainer(int page, int size) {
    List<Buchung> buchungen = buchungRepository.findAll();
    Saldo lastSaldo = getNewestSaldoService.getNewestSaldo();

    List<Saldo> saldi = aggregateSaldoService.aggregateSaldi(buchungen, lastSaldo);
    List<Saldo> filtered = saldi
      .stream()
      .skip(page * size)
      .limit(size)
      .collect(Collectors.toList());
    return SaldiContainer.builder()
      .saldi(filtered)
      .totalPages((long) Math.ceil((double) saldi.size() / size))
      .totalElements(saldi.size())
      .build();
  }
}
