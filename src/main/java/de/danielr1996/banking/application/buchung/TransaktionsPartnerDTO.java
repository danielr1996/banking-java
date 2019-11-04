package de.danielr1996.banking.application.buchung;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransaktionsPartnerDTO {
  private String iban;
  private String bic;
  private String blz;
  private String name;
}
