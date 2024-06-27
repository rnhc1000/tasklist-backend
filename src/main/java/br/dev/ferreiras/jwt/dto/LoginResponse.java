package br.dev.ferreiras.jwt.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
}
