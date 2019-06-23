package de.danielr1996.banking.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Buchung {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
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
}