package com.example.springsecuritytd3.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginContrller {
    @GetMapping("/login")
    public String viewLogin() {
        return "view-login";
    }

    @GetMapping("/hi")
    public String hi() {
        return "hi";
    }

    @GetMapping("success")
    public String sucess() {
        return "sucess";
    }

}
