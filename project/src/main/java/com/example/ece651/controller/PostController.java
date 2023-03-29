package com.example.ece651.controller;

import com.example.ece651.domain.Comment;
import com.example.ece651.domain.Post;
import com.example.ece651.domain.User;
import com.example.ece651.service.HashtagService;
import com.example.ece651.domain.homebody;
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

    @GetMapping("/home/{username}")
    public ResponseEntity<List<Post>> HomepagePosts(@PathVariable String username){
        User currentUser = userService.FindUserByUsername(username);
        List<ObjectId> followlist = currentUser.getFollows();
        List<Post> posts = new ArrayList<>();
        //First add follow's posts into posts list.
        if(followlist!=null){
            for(int j=0;j<followlist.size();j++){
                User cur_follow = userService.FindUserByUserId(followlist.get(j));
                List<Post> cur_follow_post_list = postService.getPostsByUser(cur_follow);
                posts.addAll(cur_follow_post_list);
            }
        }
        //Second add himself's posts into posts list.
        List<Post> cur_follow_post_list = postService.getPostsByUser(currentUser);
        posts.addAll(cur_follow_post_list);
        //if the size<10, add it until 10.
        if(posts.size()<10){
            List<Post> AllPosts = postService.allPosts();
            for(int index=0;index<AllPosts.size();index++){
                Post cur_post = AllPosts.get(index);
                if(!posts.contains(cur_post)){
                    posts.add(cur_post);
                    if(posts.size()>=10)
                        break;
                }
            }
        }

        List<ObjectId> like_posts = currentUser.getLike_posts();
        List<ObjectId> follow_user_id_list = currentUser.getFollows();
        List<ObjectId> saved_posts = currentUser.getSaved_posts();

        List<Post> posts_include_whether_liked= new ArrayList<>();
        if(like_posts != null) {
            for (int i = 0; i < posts.size(); i++) {
                Boolean whether_liked = false;
                Boolean whether_followed = false;
                Boolean whether_saved = false;
                Post cur_post = posts.get(i);
                ObjectId cur_post_owner_id = userService.FindUserByUsername(cur_post.getUsername()).getId();

                for(int index=0;index<like_posts.size();index++){
                    if(Objects.equals(like_posts.get(index),cur_post.getOid())){
                        whether_liked = true;
                    }
                }

                if(follow_user_id_list!= null){
                    if (follow_user_id_list.contains(cur_post_owner_id) || cur_post_owner_id.equals(currentUser.getId()))
                        whether_followed = true;
                }

                for(int index=0;index<saved_posts.size();index++){
                    if(Objects.equals(saved_posts.get(index),cur_post.getOid())){
                        whether_saved = true;
                    }
                }
                cur_post.setWhether_followed_post_user(whether_followed);
                cur_post.setWhether_liked(whether_liked);
                cur_post.setWhether_saved(whether_saved);
                //homebody home_post = new homebody(cur_post,whether_liked,whether_followed);
                posts_include_whether_liked.add(cur_post);
            }
        }
        else{
            for (int i = 0; i < posts.size(); i++) {
                Boolean whether_liked = false;
                Post cur_post = posts.get(i);

                ObjectId cur_post_owner_id = userService.FindUserByUsername(cur_post.getUsername()).getId();
                Boolean whether_followed = false;
                if(follow_user_id_list!= null){
                    if (follow_user_id_list.contains(cur_post_owner_id) || cur_post_owner_id.equals(currentUser.getId()))
                        whether_followed = true;
                }
                //homebody home_post = new homebody(cur_post,whether_liked,whether_followed);
                cur_post.setWhether_followed_post_user(whether_followed);
                cur_post.setWhether_liked(whether_liked);
                posts_include_whether_liked.add(cur_post);
            }
        }
        System.out.println(posts_include_whether_liked.size());
        ResponseEntity response = new ResponseEntity<>(posts_include_whether_liked,HttpStatus.OK);
        return response;
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
