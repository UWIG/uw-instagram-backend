package com.example.ece651.controller;

import com.example.ece651.domain.Post;
import com.example.ece651.domain.User;
import com.example.ece651.service.HashtagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin
@RequestMapping("/hashtags")
public class HashtagController {

    @Autowired
    private HashtagService hashtagService;

    @GetMapping("/{hashtag}")
    public ResponseEntity<List<Post>> getPosts(@PathVariable String hashtag){
        System.out.println(hashtag);
        List<Post> posts = hashtagService.getPostsByHashtag(hashtag);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

}
