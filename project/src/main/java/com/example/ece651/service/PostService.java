package com.example.ece651.service;

import com.example.ece651.domain.*;
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

    @Autowired
    private HashtagService hashtagService;

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
        Media avatar = user.getAvatar();
        if(user.getPostIds() != null){
            for(ObjectId id: user.getPostIds()){
                Post post = mongoTemplate.findById(id, Post.class,"post");
                post.setAvatar(avatar);
                updatePostMedia(post);
                posts.add(post);
            }
        }
        return posts;
    }

    public Comment updateCommentByComment(User user, String comment, String id){
        ObjectId commentId = new ObjectId(id);
        Comment newComment = new Comment(user.getUsername(),comment);
        mongoTemplate.insert(newComment,"comment");
        mongoTemplate.update(Comment.class).matching(Criteria.where("_id").is(commentId))
                .apply(new Update().push("replies").value(newComment.getOid()))
                .first();
        return newComment;
    }

    public Comment updatePostByComment(User user, String comment, String id){
        ObjectId postId = new ObjectId(id);
        Comment newComment = new Comment(user.getUsername(),comment);
        mongoTemplate.insert(newComment,"comment");
        mongoTemplate.update(Post.class).matching(Criteria.where("_id").is(postId))
                .apply(new Update().push("comments").value(newComment.getOid()))
                .first();
        return newComment;
    }

    public void deleteComment(Comment comment){
        if(comment.getReplies() != null){
            for(Comment reply: comment.getReplies()){
                deleteComment(reply);
            }
        }
        ObjectId id = comment.getOid();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        mongoTemplate.remove(query,Comment.class,"comment");
    }

    public Post newPost(User user, String caption, MultipartFile[] media, String hashtags) throws IOException {
        List<Media> mediaList = mediaService.createMediaList(media);
        Post post = new Post(user.getUsername(),caption,mediaList);
        if(!hashtags.equals("")){
            hashtagService.addHashtags(hashtags, post);
        }
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
        if(user.getSaved_posts() != null){
            for(ObjectId id: user.getSaved_posts()){
                Post post = FindPostByOid(id);
                post.setAvatar(userService.FindAvatarByUsername(post.getUsername()));
                updatePostMedia(post);
                posts.add(post);
            }
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

    public List<Post> getPostsByHashtag(String hashtag){
        Hashtag tag = hashtagService.getHashtag(hashtag);
        for(Post post: tag.getPostList()){
            post.setAvatar(userService.FindAvatarByUsername(post.getUsername()));
            updatePostMedia(post);
        }
        System.out.println(tag.getTag());
        return tag.getPostList();
    }

    public void deletePostByPostId(String postId){
        ObjectId id = new ObjectId(postId);
        Post post = FindPostByOid(id);
        System.out.println(post);
        if(post != null){
            if(post.getMediaList() != null){
                for(Media media: post.getMediaList()){
                    mediaService.deleteFile(media);
                }
            }
            if(post.getComments() != null){
                for(Comment comment: post.getComments()){
                    deleteComment(comment);
                }
            }
            if(post.getHashtags() != null){
                for(String tag: post.getHashtags()){
                    tag = tag.substring(1);
                    mongoTemplate.update(Hashtag.class).matching(Criteria.where("tag").is(tag))
                        .apply(new Update().pull("postList",id)).first();
                }
            }
            mongoTemplate.update(User.class).matching(Criteria.where("username").is(post.getUsername()))
                    .apply(new Update().pull("postIds",id)).first();
            mongoTemplate.remove(post);
        }
    }


    public void updatePostMedia(Post post){
        if(post.getMediaList() != null){
            for(Media media: post.getMediaList()){
                media.setData(mediaService.downloadFile(media.getFilename()));
            }
        }
        if(post.getComments() != null){
            for(Comment comment: post.getComments()){
                updateCommentMedia(comment);
            }
        }
    }

    public void updateCommentMedia(Comment comment){
        comment.setAvatar(userService.FindAvatarByUsername(comment.getUsername()));
        if(comment.getReplies() != null){
            for(Comment reply: comment.getReplies()){
                reply.setAvatar(userService.FindAvatarByUsername(reply.getUsername()));
            }
        }
    }
}


