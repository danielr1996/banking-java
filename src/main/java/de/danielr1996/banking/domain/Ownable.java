package de.danielr1996.banking.domain;

import java.util.UUID;

public interface Ownable<T> {
  public T getOwner();
}
