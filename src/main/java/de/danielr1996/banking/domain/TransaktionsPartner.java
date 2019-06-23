package de.danielr1996.banking.domain;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Embeddable
@Entity
public class TransaktionsPartner {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String iban;
    private String bic;
    private String name;
}
