package com.example.ece651.controller;

import com.example.ece651.domain.Comment;
import com.example.ece651.domain.Post;
import com.example.ece651.domain.User;
import com.example.ece651.service.HashtagService;
import com.example.ece651.service.MediaService;
import com.example.ece651.service.PostService;
import com.example.ece651.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private HashtagService hashtagService;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private MediaService mediaService;

    private User testUser;
    private Post testPost;

    private Comment testComment;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        ObjectId userId = new ObjectId();
        testUser = new User();
        testUser.setId(userId);
        testUser.setUsername("testUser");

        ObjectId postId = new ObjectId();
        testPost = new Post();
        testPost.setOid(postId);
        testPost.setUsername(testUser.getUsername());

        ObjectId commentId = new ObjectId();
        testComment = new Comment();
        testComment.setOid(commentId);
        testComment.setComment("Test comment");

        objectMapper = new ObjectMapper();
    }

    @Test
    public void getAllPostsTest() throws Exception {
        List<Post> allPosts = Arrays.asList(testPost);

        when(postService.allPosts()).thenReturn(allPosts);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value(testUser.getUsername()));
    }

    @Test
    public void getPostsTest() throws Exception {
        List<Post> userPosts = Collections.singletonList(testPost);

        when(userService.FindUserByUsername(testUser.getUsername())).thenReturn(testUser);
        when(postService.getPostsByUser(testUser)).thenReturn(userPosts);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/posts/{username}", testUser.getUsername())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value(testUser.getUsername()));
    }

    @Test
    public void HomepagePostsTest() throws Exception {
        when(userService.FindUserByUsername(testUser.getUsername())).thenReturn(testUser);
        when(postService.allPosts()).thenReturn(Collections.singletonList(testPost));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/posts/home/{username}", testUser.getUsername())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value(testUser.getUsername()));
    }

    @Test
    public void createCommentTest() throws Exception {
        String payload = objectMapper.writeValueAsString(Map.of(
                "username", testUser.getUsername(),
                "comment", testComment.getComment(),
                "id", testPost.getOid().toHexString(),
                "commentId", "",
                "reply", ""
        ));

        when(userService.FindUserByUsername(testUser.getUsername())).thenReturn(testUser);
        when(postService.updatePostByComment(testUser, testComment.getComment(), testPost.getOid().toHexString())).thenReturn(testComment);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comment").value(testComment.getComment()));
    }

    @Test
    public void createPostTest() throws Exception {
        MockMultipartFile mediaFile = new MockMultipartFile("media", "test.jpg", "image/jpeg", "test image content".getBytes());
        String caption = "Test caption";
        String hashtags = "test,example";

        when(userService.FindUserByUsername(testUser.getUsername())).thenReturn(testUser);
        when(postService.newPost(testUser, caption, new MultipartFile[]{mediaFile})).thenReturn(testPost);

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/api/posts/{username}", testUser.getUsername())
                        .file(mediaFile)
                        .param("caption", caption)
                        .param("hashtags", hashtags))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(testUser.getUsername()));
    }
}