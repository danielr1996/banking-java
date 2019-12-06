package de.danielr1996.banking.application.buchung.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuchungContainer {
  private List<BuchungDTO> buchungen;
  private long totalElements;
  private long totalPages;
}
