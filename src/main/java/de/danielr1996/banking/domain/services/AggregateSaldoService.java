package de.danielr1996.banking.domain.services;

import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.entities.Saldo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class AggregateSaldoService {
  /**
   * Summiert Einzelbuchungen zu einer Liste von Saldi.
   * @param buchungen
   * @param currentSaldo
   * @return
   */
  public static List<Saldo> aggregateSaldi(UUID kontoId, List<Buchung> buchungen, Saldo currentSaldo) {
    List<Saldo> saldi = new ArrayList<>();
    Collections.sort(buchungen, Comparator.comparing(Buchung::getValutadatum));
    Collections.reverse(buchungen);
    Saldo lastSaldo = currentSaldo;
    saldi.add(currentSaldo);
    for (Buchung buchung : buchungen) {
      Saldo s = Saldo.builder()
        .betrag(lastSaldo.getBetrag().subtract(buchung.getBetrag()))
        .datum(buchung.getValutadatum())
        .kontoId(buchung.getKontoId())
        .build();
      saldi.add(s);
      lastSaldo = s;
    }
    return saldi;
  }
}
