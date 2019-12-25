package de.danielr1996.banking.application.saldo.dto;

import de.danielr1996.banking.domain.entities.Saldo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaldiContainer {
  private List<Saldo> saldi;
  private long totalElements;
  private long totalPages;
}
