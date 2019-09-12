package de.danielr1996.banking.application;

import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.infrastructure.graphql.BuchungContainer;
import de.danielr1996.banking.domain.repository.BuchungRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PageBuchungServiceTest {
  @Autowired
  private BuchungRepository buchungRepository;

  @Autowired
  private PageBuchungService pageBuchungService;

  @Test
  void getBuchungContainer() {
    List<Buchung> buchungen = Arrays.asList(
      Buchung.builder().id("2018-06-21-04.05.07.472468").build(),
      Buchung.builder().id("2018-06-20-04.05.07.472468").build(),
      Buchung.builder().id("2018-06-19-04.05.07.472468").build(),
      Buchung.builder().id("2018-06-18-04.05.07.472468").build(),
      Buchung.builder().id("2018-06-17-04.05.07.472468").build(),
      Buchung.builder().id("2018-06-16-04.05.07.472468").build(),
      Buchung.builder().id("2018-06-15-04.05.07.472468").build(),
      Buchung.builder().id("2018-06-14-04.05.07.472468").build(),
      Buchung.builder().id("2018-06-13-04.05.07.472468").build(),
      Buchung.builder().id("2018-06-12-04.05.07.472468").build()
    );

    buchungRepository.saveAll(buchungen);

    BuchungContainer expected = BuchungContainer.builder()
      .totalElements(10)
      .totalPages(5)
      .buchungen(Arrays.asList(
        Buchung.builder().id("2018-06-17-04.05.07.472468").build(),
        Buchung.builder().id("2018-06-16-04.05.07.472468").build()
      ))
      .build();

    BuchungContainer actual = pageBuchungService.getBuchungContainer(2, 2);
    assertEquals(expected, actual);
  }

  @AfterEach
  public void cleanup(){
    buchungRepository.deleteAll();
  }
}
