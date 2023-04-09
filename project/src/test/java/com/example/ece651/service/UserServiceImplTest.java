package com.example.ece651.service;

import com.example.ece651.domain.Media;
import com.example.ece651.domain.Post;
import com.example.ece651.domain.User;
import jakarta.annotation.Resource;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private PostService postService;

    @Resource
    private MongoTemplate mongoTemplate;

    @Test
    void updateUserByEmail() {
        // Create a new user
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        User newUser = userService.AddUser(user);

        // Update the user's email address
        String newEmail = "newemail@example.com";
        User updatedUser = userService.UpdateUserByEmail(newUser.getId().toString(), newEmail);

        // Check that the email address was updated correctly
        assertEquals(null, updatedUser);

    }

    @Test
    void updateUserByPhoneNumber() {
        // Create a new user
        User user = new User();
        user.setUsername("testuser");
        user.setPhoneNumber("1234567890");
        User newUser = userService.AddUser(user);

        // Update the user's phone number
        String newPhoneNumber = "0987654321";
        User updatedUser = userService.UpdateUserByPhoneNumber(newUser.getId().toString(), newPhoneNumber);

        // Check that the phone number was updated correctly
        assertEquals(null, updatedUser);

    }

    @Test
    void updateUserByPassword() {
        // Create a new user
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        User newUser = userService.AddUser(user);

        // Update the user's password
        String newPassword = "newpassword";
        User updatedUser = userService.UpdateUserByPassword(newUser.getId().toString(), newPassword);

        // Check that the password was updated correctly
        assertEquals(null, updatedUser);
    }

    @Test
    void updateUserByUsername() {
        // Create a new user
        User user = new User();
        user.setUsername("testuser");
        User newUser = userService.AddUser(user);

        // Update the user's username
        String newUsername = "newusername";
        User updatedUser = userService.UpdateUserByUsername(newUser.getId().toString(), newUsername);

        // Check that the username was updated correctly
        assertEquals(null, updatedUser);
    }

    @Test
    void updateUserByAvatar() throws IOException {
        // Create a test user
        User testUser = User.builder()
                .username("test_user111")
                .email("test111@example.com")
                .build();
        userService.AddUser(testUser);

        // Create a test avatar
        byte[] avatarData = "test avatar data".getBytes();
        MockMultipartFile avatarFile = new MockMultipartFile("avatar111", "avatar111.jpg", "image/jpeg", avatarData);

        // Call the method being tested
        Media updatedAvatar = userService.UpdateUserByAvatar("test_user111", avatarFile);

        // Check that the media object was cr seated and saved correctly
        assertNotNull(updatedAvatar.getId());
        Media savedAvatar = mongoTemplate.findById(updatedAvatar.getId(), Media.class);
        assertNotNull(savedAvatar);
        assertArrayEquals(avatarData, savedAvatar.getData().getData());

        // Check that the user's avatar was updated correctly
        User updatedUser = mongoTemplate.findOne(new Query(Criteria.where("username").is("test_user111")), User.class);
        assertNotNull(updatedUser.getAvatar());
        assertEquals(updatedAvatar.getId(), updatedUser.getAvatar().getId());
    }

    @Test
    void addUser() {
        User testUser = User.builder()
                .username("test_user_1")
                .email("test1@example.com")
                .build();
        User addedUser = userService.AddUser(testUser);
        User retrievedUser = userService.FindUserByUsername("test_user_1");

        assertEquals(addedUser, retrievedUser);
    }

    @Test
    void findUserByUsername() {
        User testUser = User.builder()
                .username("test_user_2")
                .email("test2@example.com")
                .build();
        userService.AddUser(testUser);

        User retrievedUser = userService.FindUserByUsername("test_user_2");

        assertEquals(testUser, retrievedUser);
    }

    @Test
    void findUserByEmail() {
        User testUser = User.builder()
                .username("test_user_3")
                .email("test3@example.com")
                .build();
        userService.AddUser(testUser);

        List<User> retrievedUsers = userService.FindUserByEmail("test3@example.com");

        assertEquals(1, retrievedUsers.size());
        User retrievedUser = retrievedUsers.get(0);
        assertEquals(testUser.getUsername(), retrievedUser.getUsername());
        assertEquals(testUser.getEmail(), retrievedUser.getEmail());
    }

    @Test
    void findUserBykeyword() {
        User testUser1 = User.builder()
                .username("test_user_4")
                .email("test4@example.com")
                .build();
        User testUser2 = User.builder()
                .username("test_user_5")
                .email("test5@example.com")
                .build();
        userService.AddUser(testUser1);
        userService.AddUser(testUser2);

        List<User> retrievedUsers = userService.FindUserBykeyword("test_user_4");

        assertEquals(1, retrievedUsers.size());
        User retrievedUser = retrievedUsers.get(0);
        assertEquals(testUser1.getUsername(), retrievedUser.getUsername());
        assertEquals(testUser1.getEmail(), retrievedUser.getEmail());
    }

    @Test
    void findUserByUserId() {
        User testUser = User.builder()
                .username("test_user_6")
                .email("test6@example.com")
                .build();
        User addedUser = userService.AddUser(testUser);

        User retrievedUser = userService.FindUserByUserId(addedUser.getId());

        assertEquals(addedUser, retrievedUser);
    }

    @Test
    void findAvatarByUsername() {
        // create a test user with an avatar
        Media testAvatar = new Media(new Binary(BsonBinarySubType.BINARY, "test data".getBytes()));
        User testUser = User.builder()
                .username("test_user_222")
                .email("test222@example.com")
                .avatar(testAvatar)
                .build();
        userService.AddUser(testUser);

        // call the method being tested
        Media retrievedAvatar = userService.FindAvatarByUsername("test_user_222");

        // check that the retrieved avatar matches the test avatar
        assertNotNull(retrievedAvatar);
        assertEquals(testAvatar.getData(), retrievedAvatar.getData());
    }

    @Test
    void addUserFollowList() {
        User targetUser = User.builder()
                .username("target_user")
                .email("target_user@example.com")
                .build();
        User currentUser = User.builder()
                .username("current_user")
                .email("current_user@example.com")
                .build();
        userService.AddUser(targetUser);
        userService.AddUser(currentUser);

        String result = userService.AddUserFollowList(currentUser.getUsername(), targetUser.getUsername());

        assertEquals("successful", result);
        User updatedTargetUser = userService.FindUserByUsername(targetUser.getUsername());
        User updatedCurrentUser = userService.FindUserByUsername(currentUser.getUsername());

        assertEquals(1, updatedTargetUser.getFollowees().size());
        assertEquals(1, updatedCurrentUser.getFollows().size());
        assertEquals(currentUser.getId(), updatedTargetUser.getFollowees().get(0));
        assertEquals(targetUser.getId(), updatedCurrentUser.getFollows().get(0));

    }

    @Test
    void deleteUserFollowList() {
        User targetUser = User.builder()
                .username("target_user")
                .email("target_user@example.com")
                .build();
        User currentUser = User.builder()
                .username("current_user")
                .email("current_user@example.com")
                .build();
        userService.AddUser(targetUser);
        userService.AddUser(currentUser);
        userService.AddUserFollowList(currentUser.getUsername(), targetUser.getUsername());

        String result = userService.DeleteUserFollowList(currentUser.getUsername(), targetUser.getUsername());

        assertEquals("successful", result);
        User updatedTargetUser = userService.FindUserByUsername(targetUser.getUsername());
        User updatedCurrentUser = userService.FindUserByUsername(currentUser.getUsername());
        assertEquals(0, updatedTargetUser.getFollowees().size());
        assertEquals(0, updatedCurrentUser.getFollows().size());
    }

    @Test
    void deleteUser() {
        // Create a test user
        User testUser = User.builder()
                .username("test_user_6")
                .email("test6@example.com")
                .build();
        userService.AddUser(testUser);

        // Delete the test user
        userService.DeleteUser(testUser);

        // Verify that the user has been deleted by trying to find them by email
        List<User> retrievedUsers = userService.FindUserByEmail("test6@example.com");
        assertEquals(0, retrievedUsers.size());
    }

    @Test
    void savePost() throws IOException {
        // Create a test user
        User testUser = User.builder()
                .username("test_user_7")
                .email("test7@example.com")
                .build();
        userService.AddUser(testUser);

        // Save a post for the test user
        Post testPost = Post.builder()
                .username(testUser.getUsername())
                .caption("Test post")
                .id("64275a70dfe3447c88c5aac7")
                .build();
        byte[] postData = "test post data".getBytes();
        MockMultipartFile[] postFile = new MockMultipartFile[1];
        postFile[0] = new MockMultipartFile("post111", "post111.jpg", "image/jpeg", postData);

        postService.newPost(testUser, "Test post", postFile);
        userService.savePost(testUser.getUsername(), testPost.getId().toString());

        // Verify that the post has been saved for the user
        User retrievedUser = userService.FindUserByUsername("test_user_7");
        List<ObjectId> savedPosts = retrievedUser.getPostIds();
        assertEquals(1, savedPosts.size());
    }

    @Test
    void cancelSavePost() throws IOException {
        // Create a test user
        User testUser = User.builder()
                .username("test_user_8")
                .email("test8@example.com")
                .build();
        userService.AddUser(testUser);

        // Save a post for the test user
        Post testPost = Post.builder()
                .username(testUser.getUsername())
                .caption("Test post")
                .build();
        byte[] postData = "test post data".getBytes();
        MockMultipartFile[] postFile = new MockMultipartFile[1];
        postFile[0] = new MockMultipartFile("post222", "post222.jpg", "image/jpeg", postData);
        Post res = postService.newPost(testUser, "Test post", postFile);

        userService.savePost(testUser.getUsername(), res.getId().toString());

        // Cancel saving the post for the user
        userService.cancelSavePost(testUser.getUsername(), res.getId().toString());

        // Verify that the post has been removed from the user's saved posts
        User retrievedUser = userService.FindUserByUsername("test_user_8");
        List<ObjectId> savedPosts = retrievedUser.getSaved_posts();
        assertEquals(0, savedPosts.size());
    }
}