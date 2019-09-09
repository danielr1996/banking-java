package de.danielr1996.banking.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Buchung {
  @Id
  private String id;
  private BigDecimal betrag;
  private String waehrung;
  private LocalDate buchungstag;
  private LocalDate valutadatum;
  private String buchungstext;
  private String verwendungszweck;
//  @OneToOne(cascade = CascadeType.ALL)
//  private TransaktionsPartner selfPartner;
//  @OneToOne(cascade = CascadeType.ALL)
//  private TransaktionsPartner otherPartner;


    @EqualsAndHashCode.Exclude
    private String kategorie;
//    @EqualsAndHashCode.Exclude
//    private GeoLocation location;
//    @EqualsAndHashCode.Exclude
//    private String region;
}
