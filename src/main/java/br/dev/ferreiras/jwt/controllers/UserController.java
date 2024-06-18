package br.dev.ferreiras.jwt.controllers;

import br.dev.ferreiras.jwt.controllers.dto.CreateUserDto;
import br.dev.ferreiras.jwt.models.EnumRole;
import br.dev.ferreiras.jwt.models.User;
import br.dev.ferreiras.jwt.repository.RoleRepository;
import br.dev.ferreiras.jwt.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
public class UserController {

  private final UserRepository userRepository;

  private RoleRepository roleRepository;

  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public UserController(
          UserRepository userRepository,
          RoleRepository roleRepository,
          BCryptPasswordEncoder bCryptPasswordEncoder ) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @PostMapping("/users")
  @PreAuthorize ("hasAuthority('SCOPE_ROLE_ADMIN')")
  @Transactional
  public ResponseEntity<Void> newUser(@RequestBody CreateUserDto dto ) {

    var basicRole = roleRepository.findByName(EnumRole.ROLE_USER);


    var userDB = userRepository.findByUsername(dto.username());

    if(userDB.isPresent()) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    var user = new User();

    user.setUsername(dto.username());
    user.setPassword(bCryptPasswordEncoder.encode(dto.password()));
    user.setRoles(Set.of(basicRole));

    userRepository.save(user);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/users")
  @PreAuthorize ("hasAuthority('SCOPE_ROLE_ADMIN')")
  public ResponseEntity<List<User>> listUsers() {
    var users = userRepository.findAll();
    return ResponseEntity.ok(users);
  }
}
