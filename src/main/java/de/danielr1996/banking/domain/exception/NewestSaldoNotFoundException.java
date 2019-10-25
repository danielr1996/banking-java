package de.danielr1996.banking.domain.exception;

public class NewestSaldoNotFoundException extends Exception{
  public NewestSaldoNotFoundException(){
    super("Saldo konnte nicht gefunden werden");
  }
}
