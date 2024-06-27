package br.dev.ferreiras.jwt.controllers;

import br.dev.ferreiras.jwt.dto.LoginRequest;
import br.dev.ferreiras.jwt.dto.LoginResponse;
import br.dev.ferreiras.jwt.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/")
@CrossOrigin ("http://192.168.15.11:5000")
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

//    var user = userRepository.findByUsername(loginRequest.username());
    var email = userRepository.findByEmail(loginRequest.email());
    System.out.println("Usuario ->  " + email.get().getUsername());
    System.out.println("Password -> " + email.get().getPassword());
    System.out.println("Email -> " + email.get().getEmail());

    System.out.println("Retorno -> " + email.get().isLoginCorrect(loginRequest, bCryptPasswordEncoder));
    if (email.isEmpty() && email.get().isLoginCorrect(loginRequest, bCryptPasswordEncoder)) {

      throw new BadCredentialsException("user or password invalid!");

    }

    var expiresIn = 300L;
    var now = Instant.now();

    var scopes = email.get().getRoles()
            .stream()
            .map(role -> String.valueOf(role.getName()))
            .collect(Collectors.joining(" "));

    var userName = email.get().getUsername();

    var claims = JwtClaimsSet.builder()
            .issuer("backEndTaskList")
            .subject(email.get().getUserId().toString())
            .expiresAt(now.plusSeconds(expiresIn))
            .issuedAt(now)
            .claim("scope", scopes)
            .claim("username", userName)
            .build();

    var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
  }
}
