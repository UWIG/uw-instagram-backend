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
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;
@Service
public class PostService {


    @Autowired
    private MediaService mediaService;

    @Autowired
    private UserServiceImpl userService;

    @Resource
    private MongoTemplate mongoTemplate;

    private static final String COLLECTION_NAME = "post";

    public Post FindPostById(String id) {
        Criteria criteria = Criteria.where("id").is(id);
        Query query = new Query(criteria);
        Post post = mongoTemplate.findOne(query, Post.class, COLLECTION_NAME);
        return post;
    }

    public Post FindPostByOid(ObjectId id) {
        Criteria criteria = Criteria.where("_id").is(id);
        Query query = new Query(criteria);
        Post post = mongoTemplate.findOne(query, Post.class, COLLECTION_NAME);
        return post;
    }




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

    public List<Post> savedPosts(User user) throws IOException{
        List<Post> posts = new ArrayList<>();
        for(ObjectId id: user.getSaved_posts()){
            posts.add(FindPostByOid(id));
        }
        return posts;
    }

    public String AddLike(String user_id, String post_id) {

        ObjectId user_object_id = new ObjectId(user_id);
        ObjectId post_object_id = new ObjectId(post_id);
        //User targetUser = userService.FindUserByUserId(user_object_id);
        //Post targetPost = FindPostById(post_id);

        //Add user_id into post's likes list
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(post_object_id));

        Update update1 = new Update();
        update1.addToSet("likes",user_object_id);

        mongoTemplate.updateFirst(query, update1, Post.class);

        //Add post_id into curr_user's post_like list
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("id").is(user_object_id));

        Update update2 = new Update();
        update2.addToSet("like_posts",post_object_id);

        mongoTemplate.updateFirst(query1, update2, User.class);

        return "successful";
    }

    public String DeleteLike(String user_id, String post_id) {
        ObjectId user_object_id = new ObjectId(user_id);
        ObjectId post_object_id = new ObjectId(post_id);
        User targetUser = userService.FindUserByUserId(user_object_id);
        Post targetPost = FindPostById(post_id);

        //Delete user_id into post's likes list
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(post_object_id));

        Update update1 = new Update();
        update1.pull("likes",user_object_id);

        mongoTemplate.updateFirst(query, update1, Post.class);

        //Delete post_id into curr_user's like_post list
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("id").is(user_object_id));

        Update update2 = new Update();
        update2.pull("like_posts",post_object_id);

        mongoTemplate.updateFirst(query1, update2, User.class);

        return "successful";
    }

}


