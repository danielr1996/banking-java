package de.danielr1996.banking.application;

import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.domain.exception.NewestSaldoNotFoundException;
import de.danielr1996.banking.domain.services.AggregateSaldoService;
import de.danielr1996.banking.infrastructure.graphql.SaldiContainer;
import de.danielr1996.banking.domain.repository.BuchungRepository;
import de.danielr1996.banking.domain.repository.SaldoRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PageSaldoService {

  @Autowired
  private SaldoRepository saldoRepository;

  @Autowired
  private BuchungRepository buchungRepository;

//  @Autowired
//  private AggregateSaldoService aggregateSaldoService;

  @Autowired
  private GetNewestSaldoService getNewestSaldoService;

  public SaldiContainer getSaldiContainer(UUID kontoId, int page, int size) {
    List<Buchung> buchungen = buchungRepository
      // FIXME: In Domain/Aplication Service auslagern
      .findAll(Example.of(Buchung.builder().kontoId(kontoId).build()));
    Saldo lastSaldo = getNewestSaldoService.getNewestSaldo(kontoId);


    List<Saldo> saldi = AggregateSaldoService.aggregateSaldi(kontoId, buchungen, lastSaldo);
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
