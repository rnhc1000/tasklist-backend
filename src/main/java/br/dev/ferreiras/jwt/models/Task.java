package br.dev.ferreiras.jwt.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name="tasks")
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "task_id")
  private Long taskId;

  @ManyToOne
  @JoinColumn (name = "tb_users_user_id")
  private User user;


  private String content;

  private String status ;



  @CreationTimestamp
  private Instant createdTimeStamp;


  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Long getTaskId() {
    return taskId;
  }

  public void setTaskId(Long taskId) {
    this.taskId = taskId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Instant getCreatedTimeStamp() {
    return createdTimeStamp;
  }

  public void setCreatedTimeStamp(Instant createdTimeStamp) {
    this.createdTimeStamp = createdTimeStamp;
  }
}
