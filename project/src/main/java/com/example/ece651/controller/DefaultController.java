package com.example.ece651.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/")
public class DefaultController {
    @GetMapping
    public ResponseEntity<String> healthCheck(){
        return new ResponseEntity<>("success!", HttpStatus.OK);
    }
}

