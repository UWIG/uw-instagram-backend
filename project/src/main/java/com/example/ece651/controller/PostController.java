package com.example.ece651.controller;

import com.example.ece651.domain.Comment;
import com.example.ece651.domain.Post;
import com.example.ece651.domain.User;
import com.example.ece651.service.HashtagService;
import com.example.ece651.service.MediaService;
import com.example.ece651.service.PostService;
import com.example.ece651.service.UserServiceImpl;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.*;


@RestController
@CrossOrigin
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private HashtagService hashtagService;

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private MediaService mediaService;
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(){
        List<Post> posts = postService.allPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<Post>> getPosts(@PathVariable String username){
        System.out.println(username);
        User user = userService.FindUserByUsername(username);
        List<Post> posts = postService.getPostsByUser(user);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

//    @GetMapping("/{userid}")
//    public ResponseEntity<List<Post>> getSinglePostByUserId(@PathVariable String userid){
//        System.out.println(userid);
//        //String userid = user.get("userid");
//        List<Post> posts = postService.getPostsByUserId(userid);
//        return new ResponseEntity<>(posts, HttpStatus.OK);
//    }


    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Map<String,String> payload){
        User user = userService.FindUserByUsername(payload.get("username"));
        String comment = payload.get("comment");
        String postId = payload.get("id");
        String commentId = payload.get("commentId");
        boolean reply = payload.get("reply").length() > 0;
        if(reply){
            return new ResponseEntity<>(postService.updateCommentByComment(user,comment, commentId),HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(postService.updatePostByComment(user,comment,postId),HttpStatus.CREATED);
        }
    }

    @PostMapping("/{username}")
    public ResponseEntity<Post> createPost(@PathVariable String username, @RequestParam("media") MultipartFile[] media,
                                           @RequestParam("caption") String caption, @RequestParam("hashtags") String hashtags) throws IOException {
        System.out.println(username);
        User user = userService.FindUserByUsername(username);
        Post post = postService.newPost(user, caption, media);
        if(!hashtags.equals("")) hashtagService.addHashtags(hashtags, post);
        return new ResponseEntity<>(post,HttpStatus.CREATED);
    }
}
