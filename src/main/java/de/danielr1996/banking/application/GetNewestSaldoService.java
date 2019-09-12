package de.danielr1996.banking.application;

import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.domain.repository.SaldoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class GetNewestSaldoService {

  @Autowired
  private SaldoRepository saldoRepository;

  public Saldo getNewestSaldo() {
    return saldoRepository.findAll().stream().max(Comparator.comparing(Saldo::getDatum)).get();
  }
}
