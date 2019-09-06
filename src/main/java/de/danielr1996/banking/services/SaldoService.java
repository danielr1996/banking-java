package de.danielr1996.banking.services;

import de.danielr1996.banking.domain.Buchung;
import de.danielr1996.banking.domain.Saldo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SaldoService {
  public static List<Saldo> getSaldi(List<Buchung> buchungen, Saldo currentSaldo) {
    Collections.sort(buchungen, Comparator.comparing(Buchung::getValutadatum));
    Collections.reverse(buchungen);
    List<Saldo> saldi = new ArrayList<>();
    Saldo lastSaldo = currentSaldo;
    saldi.add(currentSaldo);
    for (Buchung buchung : buchungen) {
      Saldo s = Saldo.builder()
        .betrag(lastSaldo.getBetrag().subtract(buchung.getBetrag()))
        .datum(buchung.getValutadatum())
        .build();
      saldi.add(s);
      lastSaldo = s;
    }

    return saldi;
  }
}
