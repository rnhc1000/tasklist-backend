package br.dev.ferreiras.jwt.controllers;

import br.dev.ferreiras.jwt.dto.CreateTaskDto;
import br.dev.ferreiras.jwt.dto.FeedDto;
import br.dev.ferreiras.jwt.dto.FeedItemDto;
import br.dev.ferreiras.jwt.repository.TaskRepository;
import br.dev.ferreiras.jwt.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
  @Tag (name = "get", description = "GET methods of List of tasks(feed) of an user")
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

  @Operation (summary = "Insert a new task",
          description = "Insert a task in the list with a status of TBD-To Be Done.")
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
  @ApiResponses ({
          @ApiResponse(responseCode = "200", content = { @Content (mediaType = "application/json",
                  schema = @Schema (implementation = Task.class)) }),
          @ApiResponse (responseCode = "404", description = "Task not found",
                  content = @Content) })
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
