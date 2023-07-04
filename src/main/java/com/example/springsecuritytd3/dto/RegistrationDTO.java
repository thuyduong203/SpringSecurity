package com.example.springsecuritytd3.dto;

public class RegistrationDTO {
    private String username;
    private String password;
    private String email;
    private Integer roleId;

    public RegistrationDTO() {
        super();
    }

    public RegistrationDTO(String username, String password, String email, Integer roleId) {
        super();
        this.username = username;
        this.password = password;
        this.roleId = roleId;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString() {
        return "Registration info: username: " + this.username + " password: " + this.password + " emai: " + this.email + " roleId: " + this.roleId;
    }
}
