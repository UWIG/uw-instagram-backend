package com.example.ece651;

import com.example.ece651.domain.User;
import com.example.ece651.service.UserServiceImpl;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testcase {

    @Autowired
    UserServiceImpl userService;

    @Test
    public void insert(){
        User user = new User();
        user.setId("10");
        user.setEmail("1425340771@qq.com");
        user.setUsername("WLX");
        user.setPassword("wlx13637135553");
        user.setPhoneNumber("6478937826");
        User newUser = userService.AddUser(user);
    }
}
