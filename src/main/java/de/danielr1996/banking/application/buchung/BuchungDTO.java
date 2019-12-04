package de.danielr1996.banking.application.buchung;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuchungDTO {
  private String id;
  private BigDecimal betrag;
  private String waehrung;
  private LocalDateTime buchungstag;
  private LocalDateTime valutadatum;
  private String buchungstext;
  private String verwendungszweck;
  private KontoDTO konto;
  private UUID kontoId;
  private TransaktionsPartnerDTO selfPartner;
  private TransaktionsPartnerDTO otherPartner;
}
