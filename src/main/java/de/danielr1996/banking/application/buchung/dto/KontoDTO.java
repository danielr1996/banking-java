package de.danielr1996.banking.application.buchung.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KontoDTO {
  private UUID id;
  private String blz;
  private String passwordhash;
  private String kontonummer;
  private String userId;
}
