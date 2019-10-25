package de.danielr1996.banking.domain.entities;

import de.danielr1996.banking.domain.Ownable;
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
public class Saldo implements Ownable {
  @Id
  @GeneratedValue
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private UUID id;
  private BigDecimal betrag;
  private LocalDate datum;
  private UUID kontoId;

  @Override
  public UUID getOwner() {
    return this.kontoId;
  }
}
