package de.danielr1996.banking.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class Region {
    private String land;
    private String bundesland;
    private String landkreis;
    private String gemeinde;
}
