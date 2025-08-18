package com.crni99.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserLoginController {

    @GetMapping("/user-login")
    public String userLogin() {
        return "user-login";
    }
}
