package com.devconnect.DevConnect.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class profileController {
       @PostMapping("/api/v1/profile")
       public String createProfile() {
             
        return "Profile created successfully!";
}
}
