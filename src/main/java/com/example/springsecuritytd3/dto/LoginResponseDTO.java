package com.example.springsecuritytd3.dto;

import com.example.springsecuritytd3.entity.Account;

public class LoginResponseDTO {
    private Account user;
    private String jwt;

    public LoginResponseDTO() {
        super();
    }

    public LoginResponseDTO(Account user, String jwt) {
        this.user = user;
        this.jwt = jwt;
    }

    public Account getUser() {
        return this.user;
    }

    public void setUser(Account user) {
        this.user = user;
    }

    public String getJwt() {
        return this.jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

}
