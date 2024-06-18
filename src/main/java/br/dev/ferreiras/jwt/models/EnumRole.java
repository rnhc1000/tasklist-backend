package br.dev.ferreiras.jwt.models;

public enum EnumRole {
  ROLE_ADMIN(1L),
  ROLE_USER(2L);

  long roleId;

  EnumRole() {
  }
  EnumRole(long roleId){
    this.roleId = roleId;
  }
  public long getRoleId() {
    return roleId;
  }
}