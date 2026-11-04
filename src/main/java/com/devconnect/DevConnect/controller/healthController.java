package com.devconnect.DevConnect.controller;

import java.util.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class healthController {

    @GetMapping("/health")
    public String healthCheck() {
        return "Application is running!";
    }


    @GetMapping("/api/v1/status")
    public Map<String,Object> status() {
        Map<String,Object> status = new HashMap<>();
        status.put("status", "API is up and running!");
        status.put("timestamp", new Date());
        status.put("version", "1.0.0");
        status.put("author", "DevConnect Team");
        return status;
    }
    
}
