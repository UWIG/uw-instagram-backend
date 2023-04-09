package com.example.ece651.controller;

import com.example.ece651.domain.*;
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

    @GetMapping("/home/{username}")
    public ResponseEntity<Homepage> HomepagePosts(@PathVariable String username){
        User currentUser = userService.FindUserByUsername(username);
        Media avatar = currentUser.getAvatar();
        if(avatar != null){
            avatar.setData(mediaService.downloadFile(avatar.getFilename()));
        }

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
//        //if the size<10, add it until 10.
//        if(posts.size()<10){
//            List<Post> AllPosts = postService.allPosts();
//            for(int index=0;index<AllPosts.size();index++){
//                Post cur_post = AllPosts.get(index);
//                if(!posts.contains(cur_post)){
//                    posts.add(cur_post);
//                    if(posts.size()>=10)
//                        break;
//                }
//            }
//        }

        List<ObjectId> like_posts = currentUser.getLike_posts();
        List<ObjectId> follow_user_id_list = currentUser.getFollows();
        List<ObjectId> saved_posts = currentUser.getSaved_posts();

        for(Post post: posts){
            ObjectId oid = post.getOid();
            if(avatar != null){
                post.setAvatar(avatar);
            }
            postService.updatePostMedia(post);
            if(saved_posts != null){
                for(ObjectId saved_id: saved_posts){
                    if(Objects.equals(saved_id,oid)){
                        post.setWhether_saved(true);
                        break;
                    }
                }
            }
        }

        List<Post> posts_include_whether_liked= new ArrayList<>();
        if(like_posts != null) {
            for (int i = 0; i < posts.size(); i++) {
                Boolean whether_liked = false;
                Boolean whether_followed = false;

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

                cur_post.setWhether_followed_post_user(whether_followed);
                cur_post.setWhether_liked(whether_liked);
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
        Homepage homepage = new Homepage(posts_include_whether_liked, get_recommend_follow_list(username));
        ResponseEntity response = new ResponseEntity<>(homepage,HttpStatus.OK);
        return response;
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<Post>> getPosts(@PathVariable String username){
        System.out.println(username);
        User user = userService.FindUserByUsername(username);
        List<Post> posts = postService.getPostsByUser(user);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/explore/{username}")
    public ResponseEntity<List<Post>> getExplorePosts(@PathVariable String username){
        System.out.println(username);
        //User user = userService.FindUserByUsername(username);
        //List<Post> posts = postService.getPostsByUser(user);
        List<Post> allposts = postService.allPosts();
        List<Post> posts = new ArrayList<>();
        for(int index=0; index<allposts.size();index++){
            Post currentPost = allposts.get(index);
            if(!currentPost.getUsername().equals(username)){
                posts.add(currentPost);
            }
        }
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
        Post post = postService.newPost(user, caption, media, hashtags);
        return new ResponseEntity<>(post,HttpStatus.CREATED);
    }

    @GetMapping("/hashtags/{hashtag}")
    public ResponseEntity<List<Post>> getPostsByHashtag(@PathVariable String hashtag){
        System.out.println(hashtag);
        List<Post> posts = postService.getPostsByHashtag(hashtag);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable String postId){
        System.out.println(postId);
        postService.deletePostByPostId(postId);
        return new ResponseEntity<>("delete successful", HttpStatus.OK);
    }

    public List<Searchbody> get_recommend_follow_list(String username){
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
        return list;
    }
}
