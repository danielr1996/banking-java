package de.danielr1996.banking.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
//@IdClass(Buchung.BuchungPk.class)
public class Buchung {
//  @Id
//  @GeneratedValue(strategy = GenerationType.AUTO)
//  private UUID id;
  @Id
  private String id;
  private BigDecimal betrag;
  private String waehrung;
  private LocalDate buchungstag;
  private LocalDate valutadatum;
  private String buchungstext;
  private String verwendungszweck;
  @OneToOne(cascade = CascadeType.ALL)
  private TransaktionsPartner selfPartner;
  @OneToOne(cascade = CascadeType.ALL)
  private TransaktionsPartner otherPartner;


//    @EqualsAndHashCode.Exclude
//    private String kategorie;
//    @EqualsAndHashCode.Exclude
//    private GeoLocation location;
//    @EqualsAndHashCode.Exclude
//    private String region;

  /*public class BuchungPk implements Serializable {
    protected UUID id;
    protected String pseudoId;

    public BuchungPk(){}

    public BuchungPk(UUID id, String pseudoId){
      this.id = id;
      this.pseudoId = pseudoId;
    }
  }*/
}
