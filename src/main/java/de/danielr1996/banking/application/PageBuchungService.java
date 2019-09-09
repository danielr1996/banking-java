package de.danielr1996.banking.application;

import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.infrastructure.graphql.BuchungContainer;
import de.danielr1996.banking.infrastructure.graphql.SaldiContainer;
import de.danielr1996.banking.repository.BuchungRepository;
import de.danielr1996.banking.repository.SaldoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PageBuchungService {
  @Autowired
  private BuchungRepository buchungRepository;

  public BuchungContainer getBuchungContainer(int page, int size) {
    Page<Buchung> buchungen = buchungRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Order.desc("id"))));

    return BuchungContainer.builder()
      .buchungen(buchungen.getContent())
      .totalElements(buchungen.getTotalElements())
      .totalPages(buchungen.getTotalPages())
      .build();
  }
}
