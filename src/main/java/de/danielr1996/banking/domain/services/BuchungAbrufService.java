package de.danielr1996.banking.domain.services;

import de.danielr1996.banking.domain.entities.Konto;
import de.danielr1996.banking.domain.entities.Buchung;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
public interface BuchungAbrufService {
  Stream<Buchung> getBuchungen(Konto konto, Supplier<String> tanSp, Supplier<String> tanMediumSp);
}
