package de.danielr1996.banking.domain;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeoLocation {
    private double latitude;
    private double longitude;

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(!(obj instanceof  GeoLocation)){
            return false;
        }
        GeoLocation that = (GeoLocation)obj;
        return this.latitude == that.latitude && this.longitude == that.longitude;
    }
}
