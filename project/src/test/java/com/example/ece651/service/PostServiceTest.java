package com.example.ece651.service;

import com.example.ece651.domain.Comment;
import com.example.ece651.domain.Media;
import com.example.ece651.domain.Post;
import com.example.ece651.domain.User;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.mongodb.core.aggregation.ConditionalOperators.Switch.CaseOperator.when;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private UserServiceImpl userService;


    @Test
    void findPostById() {
        Post result = postService.FindPostById("6426032d26fc0d4aa1a44b48");
        assertNotNull(result);
        assertEquals("6426032d26fc0d4aa1a44b48", result.getId());
    }

    @Test
    void findPostByOid() {
        Post result = postService.FindPostByOid(new ObjectId("6426032d26fc0d4aa1a44b48"));
        assertNotNull(result);
        assertEquals("6426032d26fc0d4aa1a44b48", result.getId());
    }

    @Test
    void allPosts() {
        List<Post> result = postService.allPosts();
        assertNotNull(result);
    }

    @Test
    void getPostsByUser() throws IOException {
        User testUser = User.builder()
                .username("test_user_p_5")
                .email("test_user_p_5@example.com")
                .build();
        userService.AddUser(testUser);
        String caption = "Test caption";
        byte[] postData = "test post data".getBytes();
        MockMultipartFile[] media = new MockMultipartFile[1];
        media[0] = new MockMultipartFile("post555", "post555.jpg", "image/jpeg", postData);
        Post result = postService.newPost(testUser, caption, media);

        List<Post> res = postService.getPostsByUser(testUser);
        assertEquals(result, res.get(0));
    }

    @Test
    void updateCommentByComment() {
        User testUser = User.builder()
                .username("test_user_p_5")
                .email("test_user_p_5@example.com")
                .build();
        String testComment = "cmt";
        Comment res = postService.updateCommentByComment(testUser, testComment, "6431c25e0896105d212c1e1e");
        assertEquals("cmt", res.getComment());

    }

    @Test
    void updatePostByComment() {
        User testUser = User.builder()
                .username("test_user_p_5")
                .email("test_user_p_5@example.com")
                .build();
        String testComment = "cmt";
        Comment res = postService.updatePostByComment(testUser, testComment, "6431c25e0896105d212c1e1e");
        assertEquals("cmt", res.getComment());
    }

    @Test
    void newPost() throws IOException {
        User testUser = User.builder()
                .username("test_user_p_1")
                .email("test_user_p_1@example.com")
                .build();

        String caption = "Test caption";
        byte[] postData = "test post data".getBytes();
        MockMultipartFile[] media = new MockMultipartFile[1];
        media[0] = new MockMultipartFile("post111", "post111.jpg", "image/jpeg", postData);

        Post result = postService.newPost(testUser, caption, media);

        assertNotNull(result);
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testUser.getAvatar(), result.getAvatar());
        assertEquals(caption, result.getCaption());
    }

    @Test
    void savedPosts() throws IOException {

        User testUser = User.builder()
                .username("test_user_p_2")
                .email("test_user_p_2@example.com")
                .build();
        userService.AddUser(testUser);
        String caption = "Test caption";
        byte[] postData = "test post data".getBytes();
        MockMultipartFile[] media = new MockMultipartFile[1];
        media[0] = new MockMultipartFile("post222", "post222.jpg", "image/jpeg", postData);
        Post result = postService.newPost(testUser, caption, media);

        userService.savePost(testUser.getUsername(), result.getId());

        List<Post> savedPosts = postService.savedPosts(testUser);
        assertEquals(1, savedPosts.size());
    }

    @Test
    void addLike() throws IOException {
        User testUser = User.builder()
                .username("test_user_p_3")
                .email("test_user_p_3@example.com")
                .build();
        User user = userService.AddUser(testUser);
        String caption = "Test caption";
        byte[] postData = "test post data".getBytes();
        MockMultipartFile[] media = new MockMultipartFile[1];
        media[0] = new MockMultipartFile("post333", "post333.jpg", "image/jpeg", postData);
        Post result = postService.newPost(testUser, caption, media);

        String res = postService.AddLike(user.getId().toString(), result.getId());
        assertEquals("successful", res);

    }

    @Test
    void deleteLike() throws IOException {
        User testUser = User.builder()
                .username("test_user_p_4")
                .email("test_user_p_4@example.com")
                .build();
        User user = userService.AddUser(testUser);
        String caption = "Test caption";
        byte[] postData = "test post data".getBytes();
        MockMultipartFile[] media = new MockMultipartFile[1];
        media[0] = new MockMultipartFile("post444", "post444.jpg", "image/jpeg", postData);
        Post result = postService.newPost(testUser, caption, media);

        postService.AddLike(user.getId().toString(), result.getId());
        String res = postService.DeleteLike(user.getId().toString(), result.getId());
        assertEquals("successful", res);
    }
}