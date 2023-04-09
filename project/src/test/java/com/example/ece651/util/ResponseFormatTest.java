package com.example.ece651.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ResponseFormatTest {

    private ResponseFormat response;

    @BeforeEach
    public void setUp() {
        response = ResponseFormat.builder()
                .res("success")
                .sign(1)
                .msg("Operation successful")
                .build();
    }

    @Test
    public void testGetters() {
        assertEquals("success", response.getRes());
        assertEquals(1, response.getSign());
        assertEquals("Operation successful", response.getMsg());
    }

    @Test
    public void testConstructor() {
        ResponseFormat newResponse = new ResponseFormat("failure", 0, "Operation failed");
        assertEquals("failure", newResponse.getRes());
        assertEquals(0, newResponse.getSign());
        assertEquals("Operation failed", newResponse.getMsg());
    }

    @Test
    public void testEquals() {
        ResponseFormat equalResponse = ResponseFormat.builder()
                .res("success")
                .sign(1)
                .msg("Operation successful")
                .build();
        ResponseFormat differentResponse = ResponseFormat.builder()
                .res("success")
                .sign(0)
                .msg("Operation successful")
                .build();

        assertEquals(response, equalResponse);
        assertNotEquals(response, differentResponse);
    }
}
