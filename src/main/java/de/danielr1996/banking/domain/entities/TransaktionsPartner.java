package de.danielr1996.banking.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TransaktionsPartner {
    @Id
    private String iban;
    private String bic;
    private String blz;
    private String name;
}
