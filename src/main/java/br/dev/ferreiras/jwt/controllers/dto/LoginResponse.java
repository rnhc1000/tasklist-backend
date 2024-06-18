package br.dev.ferreiras.jwt.controllers.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
}
