package de.danielr1996.banking.application;

import de.danielr1996.banking.domain.MockBuchungRepository;
import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.repository.BuchungRepository;
import de.danielr1996.banking.infrastructure.graphql.BuchungContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PageBuchungServiceTest {
  private BuchungRepository buchungRepository = new MockBuchungRepository();

  private PageBuchungService pageBuchungService = new PageBuchungService(buchungRepository);

  @Test
  @Disabled
  void getBuchungContainer() {
    UUID kontoId = UUID.randomUUID();
    List<Buchung> buchungen = Arrays.asList(
      Buchung.builder().id("2018-06-21-04.05.07.472468").kontoId(kontoId).build(),
      Buchung.builder().id("2018-06-20-04.05.07.472468").kontoId(kontoId).build(),
      Buchung.builder().id("2018-06-19-04.05.07.472468").kontoId(kontoId).build(),
      Buchung.builder().id("2018-06-18-04.05.07.472468").kontoId(kontoId).build(),
      Buchung.builder().id("2018-06-17-04.05.07.472468").kontoId(kontoId).build(),
      Buchung.builder().id("2018-06-16-04.05.07.472468").kontoId(kontoId).build(),
      Buchung.builder().id("2018-06-15-04.05.07.472468").kontoId(kontoId).build(),
      Buchung.builder().id("2018-06-14-04.05.07.472468").kontoId(kontoId).build(),
      Buchung.builder().id("2018-06-13-04.05.07.472468").kontoId(kontoId).build(),
      Buchung.builder().id("2018-06-12-04.05.07.472468").kontoId(kontoId).build()
    );

    buchungRepository.saveAll(buchungen);

    BuchungContainer expected = BuchungContainer.builder()
      .totalElements(10)
      .totalPages(5)
      .buchungen(Arrays.asList(
        Buchung.builder().id("2018-06-17-04.05.07.472468").kontoId(kontoId).build(),
        Buchung.builder().id("2018-06-16-04.05.07.472468").kontoId(kontoId).build()
      ))
      .build();

    BuchungContainer actual = pageBuchungService.getBuchungContainer(kontoId, 2, 2);
    assertEquals(expected, actual);
  }

  @AfterEach
  public void cleanup() {
    buchungRepository.deleteAll();
  }
}
