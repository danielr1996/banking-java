package de.danielr1996.banking.domain.entities;

import de.danielr1996.banking.domain.Ownable;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Buchung implements Ownable {
  @Id
  private String id;
  private BigDecimal betrag;
  private String waehrung;
  private LocalDateTime buchungstag;
  private LocalDateTime valutadatum;
  private String buchungstext;
  private String verwendungszweck;
  private UUID kontoId;
  @OneToOne(cascade = CascadeType.ALL)
  private TransaktionsPartner selfPartner;
  @OneToOne(cascade = CascadeType.ALL)
  private TransaktionsPartner otherPartner;


  @EqualsAndHashCode.Exclude
  private String kategorie;

  @Override
  public UUID getOwner() {
    return this.kontoId;
  }
}
