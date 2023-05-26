package com.example.springsecuritytd3.dto;

public class RegistrationDTO {
    private String username;
    private String password;
    private Integer roleId;

    public RegistrationDTO() {
        super();
    }

    public RegistrationDTO(String username, String password, Integer roleId) {
        super();
        this.username = username;
        this.password = password;
        this.roleId = roleId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return "Registration info: username: " + this.username + " password: " + this.password + " roleId: " + this.roleId;
    }
}
