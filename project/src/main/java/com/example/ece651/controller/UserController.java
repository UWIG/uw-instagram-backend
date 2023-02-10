package com.example.ece651.controller;


import com.example.ece651.domain.User;
import com.example.ece651.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/register")
    public User registerUser(@RequestBody Map<String,String> user){
        User user1 = new User();
        user1.setId(user.get("id"));
        user1.setEmail(user.get("email"));
        user1.setUsername(user.get("username"));
        user1.setPassword(user.get("password"));
        user1.setPhoneNumber(user.get("phoneNumber"));
        return userService.AddUser(user1);
    }




}
