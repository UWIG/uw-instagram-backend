package com.example.ece651.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultControllerTest {

    @Test
    void healthCheck() {
        DefaultController controller = new DefaultController();
        ResponseEntity<String> response = controller.healthCheck();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success!", response.getBody());
    }
}