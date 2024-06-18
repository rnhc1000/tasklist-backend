package br.dev.ferreiras.jwt.config;

import br.dev.ferreiras.jwt.models.EnumRole;
import br.dev.ferreiras.jwt.models.User;
import br.dev.ferreiras.jwt.repository.RoleRepository;
import br.dev.ferreiras.jwt.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

  private RoleRepository roleRepository;

  private UserRepository userRepository;

  private BCryptPasswordEncoder bCryptPasswordEncoder;

  private PasswordEncoder passwordEncoder;
  public AdminUserConfig(
          UserRepository userRepository,
          RoleRepository roleRepository,
          BCryptPasswordEncoder bCryptPasswordEncoder,
          PasswordEncoder passwordEncoder
  ) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.passwordEncoder = passwordEncoder;
  }
  @Override
  @Transactional
  public void run(String... args) throws Exception {

//    Set<Optional<Role>> adminRoles = new LinkedHashSet<>();
    var roleAdmin = roleRepository.findByName(EnumRole.ROLE_ADMIN);
    System.out.println(roleAdmin);
//    adminRoles.add(roleAdmin);
    var userAdmin = userRepository.findByUsername("admin");
    userAdmin.ifPresentOrElse(
            user -> {
               System.out.println("admin exists");
            },
            () -> {
              var user = new User();
              user.setUsername("admin");
              user.setPassword(bCryptPasswordEncoder.encode("s0t3cht1"));
              user.setEmail("ricardo@ferreiras.dev.br");
              user.setRoles(Set.of(roleAdmin));
              userRepository.save(user);
            }
    );
  }
}
