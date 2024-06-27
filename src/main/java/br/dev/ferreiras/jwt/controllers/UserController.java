package br.dev.ferreiras.jwt.controllers;

import br.dev.ferreiras.jwt.dto.CreateUserDto;
import br.dev.ferreiras.jwt.models.EnumRole;
import br.dev.ferreiras.jwt.models.User;
import br.dev.ferreiras.jwt.repository.RoleRepository;
import br.dev.ferreiras.jwt.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
public class UserController {

  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public UserController(
          UserRepository userRepository,
          RoleRepository roleRepository,
          BCryptPasswordEncoder bCryptPasswordEncoder ) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }
  @Operation (summary = "Create a user",
          description = "Create a task list user and returns the accessToken and its expiration time.")
  @PostMapping("/users")
  @PreAuthorize ("hasAuthority('SCOPE_ROLE_ADMIN')")
  @Transactional
  public ResponseEntity<Void> newUser(@RequestBody CreateUserDto dto ) {

    var basicRole = roleRepository.findByName(EnumRole.ROLE_USER);

    var emailDB = userRepository.findByEmail(dto.email());

    var userDB = userRepository.findByUsername(dto.username());

    if(userDB.isPresent() || emailDB.isPresent()) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    var user = new User();
    user.setEmail(dto.email());
    user.setUsername(dto.username());
    user.setPassword(bCryptPasswordEncoder.encode(dto.password()));
    user.setRoles(Set.of(basicRole));

    userRepository.save(user);
    return ResponseEntity.ok().build();
  }

  @Tag (name = "get", description = "GET list of users Task List App")
  @GetMapping("/users")
//  @PreAuthorize ("hasAuthority('SCOPE_ROLE_ADMIN')")
  public ResponseEntity<List<User>> listUsers() {
    var users = userRepository.findAll();
    return ResponseEntity.ok(users);
  }
}
