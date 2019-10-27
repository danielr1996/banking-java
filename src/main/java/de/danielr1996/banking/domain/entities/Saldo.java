package de.danielr1996.banking.domain.entities;

import de.danielr1996.banking.domain.Ownable;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
  private LocalDateTime datum;
  private UUID kontoId;

  @Override
  public UUID getOwner() {
    return this.kontoId;
  }
}
