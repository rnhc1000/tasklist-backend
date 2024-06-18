package br.dev.ferreiras.jwt.services.exceptions;

public class ResourceNotFoundException extends RuntimeException{

  public ResourceNotFoundException(String msg) {
    super(msg);
  }
}
