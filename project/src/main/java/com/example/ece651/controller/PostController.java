package com.example.ece651.controller;

import com.example.ece651.domain.Comment;
import com.example.ece651.domain.Post;
import com.example.ece651.service.MediaService;
import com.example.ece651.service.PostService;
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
    private MediaService mediaService;
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(){
        List<Post> posts = postService.allPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<Post>> getPosts(@PathVariable String username){
        System.out.println(username);
        List<Post> posts = new ArrayList<Post>();
        posts = postService.getPostsByUsername(username);
        if(posts != null)
            System.out.println(posts.size());
        else
            posts = new ArrayList<Post>();
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
        return new ResponseEntity<>(postService.updatePostByComment(payload.get("comment"),payload.get("id")),HttpStatus.CREATED);
    }

    @PostMapping("/{username}")
    public ResponseEntity<Post> createPost(@PathVariable String username, @RequestParam("media") MultipartFile[] media,
                                           @RequestParam("caption") String caption,@RequestParam("avatar") String avatar ) throws IOException {
        System.out.println(username);
        return new ResponseEntity<>(postService.newPost(username, caption, avatar, media),HttpStatus.CREATED);
    }
}
