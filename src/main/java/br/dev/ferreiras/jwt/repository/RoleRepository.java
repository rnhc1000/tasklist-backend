package br.dev.ferreiras.jwt.repository;

import br.dev.ferreiras.jwt.models.EnumRole;
import br.dev.ferreiras.jwt.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Role findByName(EnumRole name);
}
