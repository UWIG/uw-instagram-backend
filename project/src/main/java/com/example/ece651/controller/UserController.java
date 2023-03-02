package com.example.ece651.controller;


import com.example.ece651.domain.User;
import com.example.ece651.service.UserServiceImpl;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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

    @PostMapping("/login")
    public String loginUser(@RequestParam("username") String username, @RequestParam("password") String password){
        //String username = user.getUsername();
        //String password = user.getPassword();
        if (username == null || password == null) {
            return "Error_NULL Exception";
        }
        List<User> userlist = userService.FindUserByUsername(username);
        for(int i=0;i<userlist.size();i++){
            User current_user = userlist.get(i);
            String cur_username = current_user.getUsername();
            String cur_password = current_user.getPassword();
            //System.out.println(password);
            //System.out.println(cur_username+" "+cur_password);
            if (cur_password.equals(password)){
                return "login successful";
            }
        }
        return "Not found this user in the system";
    }




}
