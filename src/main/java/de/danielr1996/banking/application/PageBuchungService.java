package de.danielr1996.banking.application;

import java.util.UUID;
import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.repository.BuchungRepository;
import de.danielr1996.banking.infrastructure.graphql.BuchungContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PageBuchungService {
  @Autowired
  private BuchungRepository buchungRepository;

  public BuchungContainer getBuchungContainer(UUID kontoId, int page, int size) {
    // FIXME: In Domain/Aplication Service auslagern
    Page<Buchung> buchungen = buchungRepository
      .findAll(Example.of(Buchung.builder().kontoId(kontoId).build()),PageRequest.of(page, size, Sort.by(Sort.Order.desc("id"))));
    return BuchungContainer.builder()
      .buchungen(buchungen.getContent())
      .totalElements(buchungen.getTotalElements())
      .totalPages(buchungen.getTotalPages())
      .build();
  }
}
