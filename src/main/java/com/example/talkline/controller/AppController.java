package com.example.talkline.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class AppController {

    @GetMapping("")
    public String showFirstPage(){
        return "sign-in";
    }

    @GetMapping("/notifications")
    public String showNotifications(){
        return "notifications";
    }

    @GetMapping("/messages")
    public String showMessages(){
        return "messages";
    }

    @GetMapping("/profile")
    public String showProfile(){
        return "profile";
    }

    @GetMapping("/results")
    public String showResults(){
        return "results";
    }

    @GetMapping("/settings")
    public String showSettings(){
        return "settings";
    }

    @GetMapping("/sign-in")
    public String showSignIn(){
        return "sign-in";
    }

    @GetMapping("/login")
    public String login(){
        return "sign-in";
    }
}
