package de.danielr1996.banking.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Konto {
  @Id
  private UUID id;
  private String blz;
  private String password;
  private String kontonummer;
  private String userId;
  private String secmech;
  private String tanmedia;
}
