package com.example.ece651.controller;

import com.example.ece651.domain.Post;
import com.example.ece651.domain.Media;
import com.example.ece651.domain.Searchbody;
import com.example.ece651.domain.User;
import com.example.ece651.service.MediaService;
import com.example.ece651.service.PostService;
import com.example.ece651.service.UserService;
import com.example.ece651.service.UserServiceImpl;
import org.apache.commons.codec.binary.Hex;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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

    @Autowired
    private MediaService mediaService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String,String> user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        System.out.println(user);
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
        password = encode_password(password);
        System.out.println(password);
        user1.setEmail(email);
        user1.setUsername(username);
        user1.setPassword(password);
        user1.setFullname(fullname);
        userService.AddUser(user1);
        return new ResponseEntity<>("register successful", HttpStatus.OK);
    }

    @PostMapping("/login" )
    public ResponseEntity<User> loginUser(@RequestBody Map<String,String> user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String username = user.get("username");
        String password = user.get("password");
        if (username == null || password == null) {
            return new ResponseEntity<>(new User(), HttpStatus.UNAUTHORIZED);
        }
        password = encode_password(password);
        System.out.println(password);
        User cur_user = userService.FindUserByUsername(username);
//        for(int i=0;i<userlist.size();i++){
//            User current_user = userlist.get(i);
        if(cur_user != null){
            String cur_username = cur_user.getUsername();
            String cur_password = cur_user.getPassword();
            if (cur_password.equals(password)){
                if(cur_user.getAvatar() != null){
                    Media media = cur_user.getAvatar();
                    media.setData(mediaService.downloadFile(media.getFilename()));
                }
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


    @GetMapping("/recommend/{username}")
    public ResponseEntity<User> recommendFollowUser(@PathVariable String username){
        List<Searchbody> list = new ArrayList<>();

        //get the userlist that the user has followed
        User search_user = userService.FindUserByUsername(username);
        List<ObjectId> followlist = search_user.getFollows();

        List<User> all_users = userService.AllUsers();

        for(int index = 0;index<all_users.size();index++){
            if (list.size() == 3)
                break;
            User cur_user = all_users.get(index);
            Boolean flag = false;
            if(followlist != null) {
                for (int ii = 0; ii < followlist.size(); ii++) {
                    ObjectId user_follow = followlist.get(ii);
                    //System.out.println(cur_user.getUsername()+" "+user_follow.getUsername());
                    if (Objects.equals(cur_user.getId(), user_follow))
                        flag = true;
                }
            }
            if(flag == false && !cur_user.getUsername().equals(username)){
                Searchbody searchbody = new Searchbody(cur_user.getAvatar(),flag,cur_user.getUsername());
                list.add(searchbody);
            }


        }


        ResponseEntity response = new ResponseEntity<>(list,HttpStatus.OK);
        return response;
    }

    @PostMapping("/search/{username}")
    public ResponseEntity<User> searchUser(@RequestBody Map<String,List<String>> body,@PathVariable String username){
        List<Searchbody> list = new ArrayList<>();
        List<String> keywords = body.get("keywords");

        User search_user = userService.FindUserByUsername(username);

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
                if(cur_user.getAvatar() != null){
                    Media media = cur_user.getAvatar();
                    media.setData(mediaService.downloadFile(media.getFilename()));
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
        if(user != null){
            userService.updateUserAvatar(user);
            user.setPosts(postService.getPostsByUser(user));
            userService.setFollowing(user);
            userService.setFollowers(user);
        }
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

    @PostMapping("/api/likes")
    public ResponseEntity<String> addLike(@RequestBody Map<String,Object> body){
        String username = body.get("username").toString();
        String post_id = body.get("post_id").toString();
        Boolean whether_like = Boolean.parseBoolean(body.get("like").toString());
        System.out.println(username+" "+post_id+" "+whether_like);
        User cur_user = userService.FindUserByUsername(username);
        String user_id = cur_user.getId().toHexString();
        String response;
        if(whether_like == true)
            response = postService.AddLike(user_id,post_id);
        else
            response = postService.DeleteLike(user_id,post_id);
        ResponseEntity response1 = new ResponseEntity<>(response,HttpStatus.OK);
        return response1;
    }

    @PostMapping("/api/save")
    public ResponseEntity<String> addSave(@RequestBody Map<String,String> body){
        String username = body.get("username");
        String post_id = body.get("post_id");
        Boolean saved =  Boolean.parseBoolean(body.get("save"));
        System.out.println(username+" "+post_id+" "+saved);
        if(saved) userService.savePost(username,post_id);
        else userService.cancelSavePost(username,post_id);
        return new ResponseEntity<>("success",HttpStatus.OK);
    }

    @GetMapping("/api/save/{username}")
    public ResponseEntity<List<Post>> getSavedPosts(@PathVariable String username) throws IOException{
        User user = userService.FindUserByUsername(username);
        return new ResponseEntity<>(postService.savedPosts(user),HttpStatus.OK);
    }


    @PostMapping("/user/update/{originUsername}")
    public ResponseEntity<ResponseFormat> updateUser(@PathVariable String originUsername, @RequestParam("fullname") String fullname, @RequestParam("username") String username, @RequestParam("email") String email, @RequestParam("phone") String phone, @RequestParam("gender") String gender) throws IOException {
        userService.updateUserProfile(originUsername, fullname, username, email, phone, gender);
        ResponseFormat responseFormat = new ResponseFormat("",1,"success");
        return new ResponseEntity<>(responseFormat,HttpStatus.OK);
    }

    @PostMapping("/user/changePwd/{username}")
    public ResponseEntity<ResponseFormat> changePwd(@PathVariable String username, @RequestParam("oldPwd") String oldPwd, @RequestParam("newPwd") String newPwd) throws IOException {
        Integer resultCode = userService.changePwd(username, oldPwd, newPwd);
        ResponseFormat responseFormat = null;
        if(resultCode == 1){
            responseFormat = new ResponseFormat("",1,"success");
        }
        else if(resultCode == 2){
            responseFormat = new ResponseFormat("",2,"fail: the old password is incorrect");
        }
        return new ResponseEntity<>(responseFormat,HttpStatus.OK);
    }

    //SHA1算法
    public String encode_password(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(str.getBytes("utf-8"));
        byte[] digest = md.digest();
        return String.valueOf(Hex.encodeHex(digest));
    }
}
