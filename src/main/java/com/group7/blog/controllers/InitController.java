package com.group7.blog.controllers;

import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class InitController {

    @Value("${server.api-url}")
    private String APIUrl;

    @GetMapping("")
    public Map<String, String> helloWorld() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello From MEB Team! Follow the API docs url below:");
        response.put("url", APIUrl + "/swagger-ui/index.html");
        return response;
    }
}