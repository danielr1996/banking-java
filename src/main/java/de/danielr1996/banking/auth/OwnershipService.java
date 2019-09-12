package de.danielr1996.banking.auth;

import de.danielr1996.banking.domain.Ownable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OwnershipService {
  public boolean isOwner(UUID ownerId, Ownable ownable){
    return ownable.getOwner().equals(ownerId);
  }
}
