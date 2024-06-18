package br.dev.ferreiras.jwt.models;

import jakarta.persistence.*;

@Entity
@Table (name = "tb_roles")
public class Role {
  @Id
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  @Column (name = "role_id")
  private Long roleId;
  @Enumerated (EnumType.STRING)
  @Column (name = "role")
  private EnumRole name;

  public Role() {

  }

  public Role(EnumRole name) {
    this.name = name;
  }

  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

  public EnumRole getName() {
    return name;
  }

  public void setName(EnumRole name) {
    this.name = name;
  }
}


