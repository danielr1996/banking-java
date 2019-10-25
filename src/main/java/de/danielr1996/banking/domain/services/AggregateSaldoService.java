package de.danielr1996.banking.domain.services;

import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.infrastructure.graphql.SaldiContainer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
public class AggregateSaldoService {
  /**
   * Summiert Einzelbuchungen zu einer Liste von Saldi.
   * @param buchungen
   * @param currentSaldo
   * @return
   */
  public static List<Saldo> aggregateSaldi(List<Buchung> buchungen, Saldo currentSaldo) {
    List<Saldo> saldi = new ArrayList<>();
    Collections.sort(buchungen, Comparator.comparing(Buchung::getValutadatum));
    Collections.reverse(buchungen);
    Saldo lastSaldo = currentSaldo;
    saldi.add(currentSaldo);
    for (Buchung buchung : buchungen) {
      Saldo s = Saldo.builder()
        .betrag(lastSaldo.getBetrag().subtract(buchung.getBetrag()))
        .datum(buchung.getValutadatum())
        .ownerid(buchung.getOwnerId())
        .build();
      saldi.add(s);
      lastSaldo = s;
    }
    return saldi;
  }
}
