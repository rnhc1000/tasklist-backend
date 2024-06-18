package br.dev.ferreiras.jwt.controllers;

import br.dev.ferreiras.jwt.controllers.dto.LoginRequest;
import br.dev.ferreiras.jwt.controllers.dto.LoginResponse;
import br.dev.ferreiras.jwt.models.Role;
import br.dev.ferreiras.jwt.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/")
public class TokenController {

  private final JwtEncoder jwtEncoder;

  private final UserRepository userRepository;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;


  private final PasswordEncoder passwordEncoder;

  public TokenController(
          JwtEncoder jwtEncoder,
          UserRepository userRepository,
          BCryptPasswordEncoder bCryptPasswordEncoder,
          PasswordEncoder passwordEncoder) {
    this.jwtEncoder = jwtEncoder;
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

    var user = userRepository.findByUsername(loginRequest.username());
    System.out.println("Usuario ->  " + user.get().getUsername());
    System.out.println("Password -> " + user.get().getPassword());
    System.out.println("Retorno -> " + user.get().isLoginCorrect(loginRequest, bCryptPasswordEncoder));
    if (user.isEmpty() && user.get().isLoginCorrect(loginRequest, bCryptPasswordEncoder)) {

      throw new BadCredentialsException("user or password invalid!");

    }

    var expiresIn = 300L;
    var now = Instant.now();

    var scopes = user.get().getRoles()
            .stream()
            .map(role -> String.valueOf(role.getName()))
            .collect(Collectors.joining(" "));

    var claims = JwtClaimsSet.builder()
            .issuer("backEndTaskList")
            .subject(user.get().getUserId().toString())
            .expiresAt(now.plusSeconds(expiresIn))
            .issuedAt(now)
            .claim("scope", scopes)
            .build();

    var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

    return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
  }
}
