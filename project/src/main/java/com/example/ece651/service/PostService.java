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


    public List<Post> allPosts(){
        return mongoTemplate.findAll(Post.class,"post");
    }

    public List<Post> getPostsByUser(User user){
        List<Post> posts = new ArrayList<Post>();
        if(user.getPostIds() != null){
            for(ObjectId id: user.getPostIds()){
                posts.add(mongoTemplate.findById(id, Post.class,"post"));
            }
        }
        return posts;
    }

    public Comment updateCommentByComment(User user, String comment, String id){
        ObjectId commentId = new ObjectId(id);
        Comment newComment = new Comment(user.getUsername(),user.getAvatar(),comment);
        mongoTemplate.insert(newComment,"comment");
        mongoTemplate.update(Comment.class).matching(Criteria.where("_id").is(commentId))
                .apply(new Update().push("replies").value(newComment.getOid()))
                .first();
        return newComment;
    }

    public Comment updatePostByComment(User user, String comment, String id){
        ObjectId postId = new ObjectId(id);
        Comment newComment = new Comment(user.getUsername(),user.getAvatar(),comment);
        mongoTemplate.insert(newComment,"comment");
        mongoTemplate.update(Post.class).matching(Criteria.where("_id").is(postId))
                .apply(new Update().push("comments").value(newComment.getOid()))
                .first();
        return newComment;
    }

    public Post newPost(User user, String caption, MultipartFile[] media) throws IOException {
        List<Media> mediaList = mediaService.createMediaList(media);
        Post post = new Post(user.getUsername(), user.getAvatar(),caption,mediaList);
        mongoTemplate.insertAll(mediaList);
        mongoTemplate.insert(post,"post");
        //change the logic to id afterwards. Currently use username.
        mongoTemplate.update(User.class).matching(Criteria.where("username").is(user.getUsername()))
                .apply(new Update().push("postIds").value(post.getOid()))
                .first();
        return post;
    }

}


