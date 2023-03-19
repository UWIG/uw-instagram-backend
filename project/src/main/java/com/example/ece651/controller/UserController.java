package com.example.ece651.controller;


import com.example.ece651.domain.Media;
import com.example.ece651.domain.Post;
import com.example.ece651.domain.Searchbody;
import com.example.ece651.domain.User;
import com.example.ece651.service.PostService;
import com.example.ece651.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.java.Log;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.example.ece651.util.ResponseFormat;

@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PostService postService;

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

        User current_user = userService.FindUserByUsername(username);
        if(current_user != null){
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
            return new ResponseEntity<>(new User(), HttpStatus.UNAUTHORIZED);
        }
        User cur_user = userService.FindUserByUsername(username);
//        for(int i=0;i<userlist.size();i++){
//            User current_user = userlist.get(i);
        if(cur_user != null){
            String cur_username = cur_user.getUsername();
            String cur_password = cur_user.getPassword();
            if (cur_password.equals(password)){
                return new ResponseEntity<>(cur_user, HttpStatus.OK);
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

        //return new ResponseEntity<>("Not found this user in the system", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/search/{username}")
    public ResponseEntity<User> searchUser(@RequestBody Map<String,List<String>> body,@PathVariable String username){
        List<Searchbody> list = new ArrayList<>();
        List<String> keywords = body.get("keywords");

        User search_user = userService.FindUserByUsername(username);

        //ObjectMapper objectMapper = new ObjectMapper();

        for(int i=0;i<keywords.size();i++){
            if(keywords.get(i) == "")
                continue;
            List<User> list1 = userService.FindUserBykeyword(keywords.get(i));
            for(int j=0;j<list1.size();j++){
                User cur_user = list1.get(j);
                List<ObjectId> followlist = search_user.getFollows();
                Boolean flag = false;
                if(followlist != null) {
                    for (int ii = 0; ii < followlist.size(); ii++) {
                        ObjectId user_follow = followlist.get(ii);
                        //System.out.println(cur_user.getUsername()+" "+user_follow.getUsername());
                        if (Objects.equals(cur_user.getId(), user_follow))
                            flag = true;
                    }
                }

                Searchbody searchbody = new Searchbody(cur_user.getAvatar(),flag,cur_user.getUsername());
                if(!list.contains(searchbody) && !Objects.equals(cur_user.getId(), search_user.getId())){
                    list.add(searchbody);
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
        User user = userService.FindUserByUsername(username);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @PostMapping("/setFollow")
    public ResponseEntity<String> setFollow(@RequestBody Map<String,String> body){

        String response = userService.AddUserFollowList(body.get("currentUserName"),body.get("targetUserName"));
        ResponseEntity response1 = new ResponseEntity<>(response,HttpStatus.OK);
        return response1;
    }

    @PostMapping("/cancelFollow")
    public ResponseEntity<String> cancelFollow(@RequestBody Map<String,String> body){

        String response = userService.DeleteUserFollowList(body.get("currentUserName"),body.get("targetUserName"));
        ResponseEntity response1 = new ResponseEntity<>(response,HttpStatus.OK);
        return response1;
    }



}
