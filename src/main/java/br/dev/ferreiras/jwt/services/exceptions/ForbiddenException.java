package br.dev.ferreiras.jwt.services.exceptions;

public class ForbiddenException extends RuntimeException{
  public ForbiddenException(String msg) {
    super(msg);
  }
}
