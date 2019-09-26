package de.danielr1996.banking.application;

import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.repository.BuchungRepository;
import de.danielr1996.banking.infrastructure.graphql.BuchungContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
