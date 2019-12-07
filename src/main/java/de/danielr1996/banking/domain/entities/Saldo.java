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
public class Saldo implements Ownable<UUID> {
  @Id
  @GeneratedValue
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private UUID kontoId;
  //  private UUID id;
  private BigDecimal betrag;
  private LocalDateTime datum;

  @Override
  public UUID getOwner() {
    return this.kontoId;
  }

  public Saldo add(Saldo other) {
    return Saldo.builder()
//      .id(UUID.randomUUID())
      .betrag(this.betrag.add(other.betrag))
      .datum(this.datum.isAfter(other.datum) ? this.datum : other.datum)
      .kontoId(this.kontoId)
      .build();
  }
}
