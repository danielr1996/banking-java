package de.danielr1996.banking.domain.entities;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.kapott.hbci.structures.Value;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Tag("unit")
class SaldoMapping {

  @Test
  void saldo1AddNull() {
    ModelMapper modelMapper = new ModelMapper();
    org.kapott.hbci.structures.Saldo saldo = new org.kapott.hbci.structures.Saldo();
    saldo.timestamp = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
    saldo.value = new Value(100, "EUR");
  }
}
