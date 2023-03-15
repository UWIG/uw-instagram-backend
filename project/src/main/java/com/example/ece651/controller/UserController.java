package com.example.ece651.controller;


import com.example.ece651.domain.Media;
import com.example.ece651.domain.Post;
import com.example.ece651.domain.User;
import com.example.ece651.service.UserServiceImpl;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.ece651.util.ResponseFormat;

@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String,String> user){
        User user1 = new User();

        String email = user.get("emailAddress");
        String username = user.get("username");
        String fullname = user.get("fullName");
        String password = user.get("password");

        if (username == "" || password == "" || fullname == "" || email == "") {
            return new ResponseEntity<>("Error NUll value", HttpStatus.UNAUTHORIZED);
        }

        List<User> userlist1 = userService.FindUserByUsername(username);
        if(userlist1.size() != 0){
            System.out.println("Duplicate Username");
            return new ResponseEntity<>("username duplicate", HttpStatus.UNAUTHORIZED);
        }
        List<User> userlist2 = userService.FindUserByEmail(email);
        if(userlist2.size() != 0){
            System.out.println("Duplicate Email");
            return new ResponseEntity<>("email duplicate", HttpStatus.UNAUTHORIZED);
        }
        //user1.setId(user.get("id"));
        user1.setEmail(email);
        user1.setUsername(username);
        user1.setPassword(password);
        user1.setFullname(fullname);
        userService.AddUser(user1);
        return new ResponseEntity<>("register successful", HttpStatus.OK);
    }

    @PostMapping("/login" )
    public ResponseEntity<User> loginUser(@RequestBody Map<String,String> user){
        String username = user.get("username");
        String password = user.get("password");
        if (username == null || password == null) {
//            return new ResponseEntity<>("Error NUll value", HttpStatus.UNAUTHORIZED);
        }
        List<User> userlist = userService.FindUserByUsername(username);
        for(int i=0;i<userlist.size();i++){
            User current_user = userlist.get(i);
            String cur_username = current_user.getUsername();
            String cur_password = current_user.getPassword();
            //System.out.println(password);
            //System.out.println(cur_username+" "+cur_password);
            if (cur_password.equals(password)){
                return new ResponseEntity<>(current_user, HttpStatus.OK);
            }
        }

        List<User> userlist1 = userService.FindUserByEmail(username);
        for(int i=0;i<userlist1.size();i++) {
            User current_user = userlist1.get(i);
            //String cur_username = current_user.getUsername();
            String cur_password = current_user.getPassword();
            //System.out.println(password);
            //System.out.println(cur_username+" "+cur_password);
            if (cur_password.equals(password)) {
                return new ResponseEntity<>(current_user, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(new User(),HttpStatus.UNAUTHORIZED);
//
//        return new ResponseEntity<>("Not found this user in the system", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/search")
    public ResponseEntity<User> searchUser(@RequestBody Map<String,List<String>> body){
        List<User> list = new ArrayList<>();
        List<String> keywords = body.get("keywords");
        for(int i=0;i<keywords.size();i++){
            List<User> list1 = userService.FindUserBykeyword(keywords.get(i));
            for(int j=0;j<list1.size();j++){
                if(!list.contains(list1.get(j))){
                    list.add(list1.get(j));
                }
            }
        }
        System.out.println(list.size());
        ResponseEntity response = new ResponseEntity<>(list,HttpStatus.OK);
        return response;
    }

    @PostMapping("/user/changeAva")
    public ResponseEntity<ResponseFormat> changeAvatar(@RequestParam("username") String username, @RequestParam("avatar") MultipartFile avatar) throws IOException {
        System.out.println(username);
        System.out.println(avatar);
        Media media = userService.UpdateUserByAvatar(username, avatar);
        ResponseFormat responseFormat = new ResponseFormat(media,1,"success");
        return new ResponseEntity<>(responseFormat,HttpStatus.OK);
    }

    @GetMapping("/{username}/avatar")
    public ResponseEntity<Media> getAvatar(@PathVariable String username) throws IOException {
        System.out.println(username);
        Media media = userService.FindAvatarByUsername(username);
        return new ResponseEntity<>(media,HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) throws IOException {
        System.out.println(username);
        User user = userService.FindUserByUsername(username).get(0);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

}
