package com.example.ece651.controller;

import com.example.ece651.util.ResponseFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import com.example.ece651.domain.Post;
import com.example.ece651.domain.Media;
import com.example.ece651.domain.User;
import com.example.ece651.service.PostService;
import com.example.ece651.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private PostService postService;

    private User testUser;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        ObjectId userId = new ObjectId();
        testUser = new User();
        testUser.setId(userId);
        testUser.setUsername("testUser");
        testUser.setPassword("testPassword");
        testUser.setFullname("Test Fullname");
        testUser.setEmail("test@example.com");

        objectMapper = new ObjectMapper();
    }

    @Test
    void registerUser() throws Exception {
        String email = "test@email.com";
        String username = "testuser";
        String fullname = "Test User";
        String password = "password";

        // User is not found in the system
        when(userService.FindUserByUsername(username)).thenReturn(null);
        when(userService.FindUserByEmail(email)).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "\"emailAddress\":\"" + email + "\","
                                + "\"username\":\"" + username + "\","
                                + "\"fullName\":\"" + fullname + "\","
                                + "\"password\":\"" + password + "\""
                                + "}"))
                .andExpect(status().isOk())
                .andExpect(content().string("register successful"));

        // Verify the user was added
        verify(userService, times(1)).AddUser(any(User.class));
    }

    @Test
    void loginUser() throws Exception {
        String username = "testuser";
        String password = "password";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        when(userService.FindUserByUsername(username)).thenReturn(user);
        when(userService.FindUserByEmail(username)).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "\"username\":\"" + username + "\","
                                + "\"password\":\"" + password + "\""
                                + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.password").value(password));
    }

    @Test
    void searchUser() throws Exception {
        String keyword = "test";
        String requestBody = objectMapper.writeValueAsString(Map.of(
                "keywords", Arrays.asList(keyword)
        ));

        when(userService.FindUserByUsername(testUser.getUsername())).thenReturn(testUser);
        when(userService.FindUserBykeyword(keyword)).thenReturn(Arrays.asList(testUser));

        mockMvc.perform(post("/search/{username}", testUser.getUsername())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

    }

    @Test
    void changeAvatar() throws Exception {
        MockMultipartFile avatar = new MockMultipartFile("avatar", "avatar.png", "image/png", "avatar".getBytes());

        Media media = new Media(new Binary("avatar".getBytes()));
        ResponseFormat responseFormat = new ResponseFormat(media, 1, "success");

        when(userService.UpdateUserByAvatar(testUser.getUsername(), avatar)).thenReturn(media);

        mockMvc.perform(multipart("/user/changeAva")
                        .file(avatar)
                        .param("username", testUser.getUsername()))
                .andExpect(status().isOk());
    }

    @Test
    void getAvatar() throws Exception {
        Media media = new Media(new Binary("avatar".getBytes()));

        when(userService.FindAvatarByUsername(testUser.getUsername())).thenReturn(media);

        mockMvc.perform(get("/{username}/avatar", testUser.getUsername()))
                .andExpect(status().isOk());

    }

    @Test
    void getUser() throws Exception {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        List<Post> posts = Arrays.asList(new Post(), new Post());

        when(userService.FindUserByUsername(username)).thenReturn(user);
        when(postService.getPostsByUser(user)).thenReturn(posts);

        mockMvc.perform(get("/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.posts.length()").value(posts.size()));
    }

    @Test
    void setFollow() throws Exception {
        String currentUsername = "currentuser";
        String targetUsername = "targetuser";

        when(userService.AddUserFollowList(currentUsername, targetUsername)).thenReturn("followed");

        mockMvc.perform(post("/setFollow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "\"currentUserName\":\"" + currentUsername + "\","
                                + "\"targetUserName\":\"" + targetUsername + "\""
                                + "}"))
                .andExpect(status().isOk())
                .andExpect(content().string("followed"));

        // Verify the follow was added
        verify(userService, times(1)).AddUserFollowList(currentUsername, targetUsername);
    }


    @Test
    void cancelFollow() throws Exception {
        String currentUsername = "currentuser";
        String targetUsername = "targetuser";

        when(userService.DeleteUserFollowList(currentUsername, targetUsername)).thenReturn("unfollowed");

        mockMvc.perform(post("/cancelFollow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "\"currentUserName\":\"" + currentUsername + "\","
                                + "\"targetUserName\":\"" + targetUsername + "\""
                                + "}"))
                .andExpect(status().isOk())
                .andExpect(content().string("unfollowed"));

        // Verify the follow was removed
        verify(userService, times(1)).DeleteUserFollowList(currentUsername, targetUsername);
    }


    @Test
    void addLike() throws Exception {
        String username = "testuser";
        String postId = "testpostid";
        String like = "true";
        User user = new User();
        user.setId(new ObjectId());
        when(userService.FindUserByUsername(username)).thenReturn(user);
        when(postService.AddLike(user.getId().toHexString(), postId)).thenReturn("success");

        mockMvc.perform(post("/api/likes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "\"username\":\"" + username + "\","
                                + "\"post_id\":\"" + postId + "\","
                                + "\"like\":\"" + like + "\""
                                + "}"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
    }

    @Test
    void addSave() throws Exception {
        String username = "testuser";
        String postId = "testpostid";
        String save = "true";

        mockMvc.perform(post("/api/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "\"username\":\"" + username + "\","
                                + "\"post_id\":\"" + postId + "\","
                                + "\"save\":\"" + save + "\""
                                + "}"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        if (Boolean.parseBoolean(save)) {
            verify(userService, times(1)).savePost(username, postId);
        } else {
            verify(userService, times(1)).cancelSavePost(username, postId);
        }
    }

    @Test
    void getSavedPosts() throws Exception {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        List<Post> posts = Arrays.asList(new Post(), new Post());

        when(userService.FindUserByUsername(username)).thenReturn(user);
        when(postService.savedPosts(any(User.class))).thenReturn(posts);

        mockMvc.perform(get("/api/save/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(posts.size()));
    }
}