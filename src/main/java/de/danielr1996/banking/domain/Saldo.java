package de.danielr1996.banking.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Saldo {
  @Id
  @GeneratedValue
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private UUID id;
  private BigDecimal betrag;
  private LocalDate datum;

}
