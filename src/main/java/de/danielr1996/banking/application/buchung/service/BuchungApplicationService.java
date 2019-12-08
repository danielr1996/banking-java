package de.danielr1996.banking.application.buchung.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import de.danielr1996.banking.application.buchung.dto.BuchungContainer;
import de.danielr1996.banking.application.buchung.dto.BuchungDTO;
import de.danielr1996.banking.application.buchung.dto.TransaktionsPartnerDTO;
import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.repository.BuchungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BuchungApplicationService {
  private BuchungRepository buchungRepository;

  public BuchungApplicationService(@Autowired BuchungRepository buchungRepository) {
    this.buchungRepository = buchungRepository;
  }

  public Optional<Buchung> findById(String id){
    return buchungRepository.findById(id);
  }

  public BuchungContainer getBuchungContainer(List<UUID> kontoIds, int page, int size) {
    Page<Buchung> buchungen = buchungRepository.findByKontoIdIn(kontoIds, PageRequest.of(page, size, Sort.by(Sort.Order.desc("id"))));

    return BuchungContainer.builder()
      .buchungen(buchungen.getContent().stream().map(buchung-> BuchungDTO.builder()
        .id(buchung.getId())
        .betrag(buchung.getBetrag())
        .waehrung(buchung.getWaehrung())
        .buchungstag(buchung.getBuchungstag())
        .valutadatum(buchung.getValutadatum())
        .buchungstext(buchung.getBuchungstext())
        .verwendungszweck(buchung.getVerwendungszweck())
        .kontoId(buchung.getKontoId())
        .selfPartner(TransaktionsPartnerDTO.builder()
          .iban(buchung.getSelfPartner().getIban())
          .bic(buchung.getSelfPartner().getBic())
          .blz(buchung.getSelfPartner().getBlz())
          .name(buchung.getSelfPartner().getName())
          .build())
        /*.otherPartner(TransaktionsPartnerDTO.builder()
          .iban(buchung.getOtherPartner().getIban())
          .bic(buchung.getOtherPartner().getBic())
          .blz(buchung.getOtherPartner().getBlz())
          .name(buchung.getOtherPartner().getName())
          .build())*/
        .build()).collect(Collectors.toList()))
      .totalElements(buchungen.getTotalElements())
      .totalPages(buchungen.getTotalPages())
      .build();
  }
}
