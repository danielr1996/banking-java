package de.danielr1996.banking.application.buchung;

import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.repository.BuchungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuchungService {
  @Autowired
  BuchungRepository buchungRepository;

  public Buchung findById(String id){
    return buchungRepository.findById(id).get();
  }
}
