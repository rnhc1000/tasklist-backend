package br.dev.ferreiras.jwt.controllers;

import br.dev.ferreiras.jwt.controllers.dto.CreateTaskDto;
import br.dev.ferreiras.jwt.controllers.dto.FeedDto;
import br.dev.ferreiras.jwt.controllers.dto.FeedItemDto;
import br.dev.ferreiras.jwt.repository.TaskRepository;
import br.dev.ferreiras.jwt.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import br.dev.ferreiras.jwt.models.Task;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
public class TaskController {
  private final TaskRepository taskRepository;

  private final UserRepository userRepository;

  public TaskController(TaskRepository taskRepository, UserRepository userRepository) {
    this.taskRepository = taskRepository;
    this.userRepository = userRepository;
  }

  @GetMapping ("/feed")
  public ResponseEntity<FeedDto> feed(@RequestParam (value = "page", defaultValue = "0") int page,
                                      @RequestParam (value = "pageSize", defaultValue = "10") int pageSize) {

    var tasks = taskRepository.findAll(PageRequest.of(page, pageSize, Sort.Direction.DESC,"createdTimeStamp"))
            .map(task -> new FeedItemDto(task.getTaskId(), task.getContent(), task.getUser().getUsername()));

    return ResponseEntity.ok(new FeedDto(tasks.getContent(),
            page,
            pageSize,
            tasks.getTotalPages(),
            tasks.getTotalElements()
    ));
  }

  @PostMapping ("/tasks")
  public ResponseEntity<Void> createTask(@RequestBody CreateTaskDto createTaskDto,
                                         JwtAuthenticationToken accessToken) {

    var user = userRepository.findById(UUID.fromString(accessToken.getName()));
    var task = new Task();
    task.setUser(user.get());
    task.setStatus(createTaskDto.status());
    task.setContent(createTaskDto.task());

    taskRepository.save(task);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping ("/tasks/{task_id}")
  public ResponseEntity<Void> deleteTask(@PathVariable ("task_id") Long taskId,
                                         JwtAuthenticationToken accessToken) {
    var task = taskRepository.findById(taskId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    if (task.getUser().getUserId().equals(UUID.fromString(accessToken.getName()))) {

      taskRepository.deleteById(taskId);

    } else {

      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    return ResponseEntity.ok().build();
  }
  //  @DeleteMapping("/tasks")
  //  public ResponseEntity<Void> deleteAllTasks(JwtAuthenticationToken accessToken) {
  //    var username = userRepository.findById(UUID.fromString(accessToken.getName()));
  //    taskRepository.deleteAllById(String.valueOf(username));
  //
  //    return ResponseEntity.ok().build();
  //  }

}
