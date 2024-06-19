package br.dev.ferreiras.jwt.models;

import br.dev.ferreiras.jwt.controllers.dto.LoginRequest;
import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "password"),
                @UniqueConstraint(columnNames = "email"),
        })
public class User {

  @Id
  @GeneratedValue (strategy = GenerationType.UUID)
    private UUID userId;

  @NotBlank
  @Column(unique = true)
  @Size (min=4, max = 20)
  private String username;

  @NotBlank
  @Email
  @Size (min=8, max = 40)
  private String email;

  @NotBlank
  @Size (min=6, max = 120)
  private String password;

  public User() {
  }

  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }
  @ManyToMany (fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinTable(name = "tb_user_roles",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public boolean isLoginCorrect(LoginRequest loginRequest, BCryptPasswordEncoder bCryptPasswordEncoder) {
    return bCryptPasswordEncoder.matches(loginRequest.password(), this.getPassword());
  }
}
