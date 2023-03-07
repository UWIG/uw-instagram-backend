package com.example.ece651.service;

import com.example.ece651.domain.Comment;
import com.example.ece651.domain.Media;
import com.example.ece651.domain.Post;
import com.example.ece651.domain.User;
import jakarta.annotation.Resource;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;
@Service
public class PostService {


    @Autowired
    private MediaService mediaService;

    @Resource
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserServiceImpl userService;

    public List<Post> allPosts(){
        return mongoTemplate.findAll(Post.class,"post");
    }

    public List<Post> getPostssByUsername(String username){
        User user = userService.FindUserByUsername(username).get(0);
        //user.getPosts();
        return user.getPosts();
    }

    public List<Post> getPostsByUserId(String id){
        ObjectId id_transfer = new ObjectId(id);
        User user = userService.FindUserByUserId(id_transfer);
        //user.getPosts();
        return user.getPosts();
    }

//    public Optional<Post> singlePost(String username){
//        return postRepository.findPostByUsername(username);
//    }

    public Comment updatePostByComment(String comment, String id){
        ObjectId postId = new ObjectId(id);
        Comment newComment = new Comment(comment);
        mongoTemplate.insert(newComment,"comment");
        mongoTemplate.update(Post.class).matching(Criteria.where("_id").is(id))
                .apply(new Update().push("comments").value(newComment.getId()))
                .first();
        return newComment;
    }

    public Post newPost(String username, String caption, String avatar, MultipartFile[] media) throws IOException {
        List<Media> mediaList = mediaService.createMediaList(media);
        Post post = new Post(caption,mediaList);
        mongoTemplate.insertAll(mediaList);
        mongoTemplate.insert(post,"post");
        //change the logic to id afterwards. Currently use username.
        mongoTemplate.update(User.class).matching(Criteria.where("username").is(username))
                .apply(new Update().push("posts").value(post.getOid()))
                .first();
        return post;
    }

}


