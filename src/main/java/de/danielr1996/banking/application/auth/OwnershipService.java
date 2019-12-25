package de.danielr1996.banking.application.auth;

import de.danielr1996.banking.domain.Ownable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OwnershipService {
  public boolean isOwner(UUID requesterId, Ownable ownable){
    return ownable.getOwner().equals(requesterId);
  }
}
