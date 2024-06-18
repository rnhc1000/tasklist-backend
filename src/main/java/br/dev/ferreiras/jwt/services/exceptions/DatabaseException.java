package br.dev.ferreiras.jwt.services.exceptions;

public class DatabaseException extends RuntimeException {

  public DatabaseException(String msg) {
    super(msg);
  }
}
