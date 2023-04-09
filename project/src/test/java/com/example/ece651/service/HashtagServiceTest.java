package com.example.ece651.service;

import com.example.ece651.domain.Hashtag;
import com.example.ece651.domain.Post;
import com.example.ece651.domain.User;
import jakarta.annotation.Resource;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

import com.example.ece651.domain.Post;
import com.example.ece651.service.HashtagService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mock.web.MockMultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class HashtagServiceTest {
    @Autowired
    private HashtagService hashtagService;
    @Autowired
    private PostService postService;
    @Autowired
    private UserServiceImpl userService;

    @Resource
    private MongoTemplate mongoTemplate;

    @Test
    void addHashtags() throws IOException {
//        Post post = new Post();

        User testUser = User.builder()
                .username("test_user_1")
                .email("test1@example.com")
                .build();
        userService.AddUser(testUser);

        Post testPost = Post.builder()
                .username(testUser.getUsername())
                .caption("Test post")
                .id("64275a70dfe3447c88c5bbc7")
                .oid(new ObjectId())
                .build();

        byte[] postData = "test post data".getBytes();
        MockMultipartFile[] postFile = new MockMultipartFile[1];
        postFile[0] = new MockMultipartFile("post111", "post111.jpg", "image/jpeg", postData);

        postService.newPost(testUser, "Test post", postFile);
        userService.savePost(testUser.getUsername(), testPost.getId().toString());

        String hashtags = "#example1,#example2";
        hashtagService.addHashtags(hashtags, testPost);


        Hashtag hashtag1 = new Hashtag("#example1", testPost);
        Hashtag hashtag2 = new Hashtag("#example2", testPost);


        List<Post> result = hashtagService.getPostsByHashtag("#example2");

        Assertions.assertEquals(1, result.size());
    }

    @Test
    void getHashtag() throws IOException {
        User testUser = User.builder()
                .username("test_user_tag_2")
                .email("test_tag_2@example.com")
                .build();
        userService.AddUser(testUser);

        Post testPost = Post.builder()
                .username(testUser.getUsername())
                .caption("Test post")
                .id("64275a70dfe3447c88c5bbc8")
                .oid(new ObjectId())
                .build();

        String hashtags = "#test1";
        hashtagService.addHashtags(hashtags, testPost);

        Hashtag retrievedHashtag = hashtagService.getHashtag("test1");
        Assertions.assertEquals("#test1", retrievedHashtag.getTag());
    }

    @Test
    void getPostsByHashtag() throws IOException {
        User testUser = User.builder()
                .username("test_user_tag_3")
                .email("test_tag_3@example.com")
                .build();
        userService.AddUser(testUser);

        Post testPost = Post.builder()
                .username(testUser.getUsername())
                .caption("Test post")
                .id("64275a70dfe3447c88c5bbc9")
                .oid(new ObjectId())
                .build();

        String hashtags = "#test2";
        hashtagService.addHashtags(hashtags, testPost);

        List<Post> posts = hashtagService.getPostsByHashtag("#test2");
        Assertions.assertEquals(1, posts.size());
//        Assertions.assertEquals(testPost.getId(), posts.get(0).getId());
    }
}