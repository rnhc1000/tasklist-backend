package br.dev.ferreiras.jwt.repository;

import br.dev.ferreiras.jwt.models.Task;
import br.dev.ferreiras.jwt.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
