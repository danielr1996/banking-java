package de.danielr1996.banking.application.buchung;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.repository.BuchungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class PageBuchungService {
  private BuchungRepository buchungRepository;

  public PageBuchungService(@Autowired BuchungRepository buchungRepository) {
    this.buchungRepository = buchungRepository;
  }

  public BuchungContainer getBuchungContainer(List<UUID> kontoIds, int page, int size) {
    Page<Buchung> buchungen = buchungRepository.findByKontoIdIn(kontoIds, PageRequest.of(page, size, Sort.by(Sort.Order.desc("id"))));

    return BuchungContainer.builder()
      .buchungen(buchungen.getContent())
      .totalElements(buchungen.getTotalElements())
      .totalPages(buchungen.getTotalPages())
      .build();
  }
}
