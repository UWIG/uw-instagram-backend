package com.example.ece651.domain;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.mapping.DBRef;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {

    @Mock
    private Media avatarMock;

    @Mock
    private Post postMock;

    private User user;

    @Before
    public void setUp() {
        List<ObjectId> followees = new ArrayList<ObjectId>();
        List<ObjectId> follows = new ArrayList<ObjectId>();
        List<ObjectId> postIds = new ArrayList<ObjectId>();
        postIds.add(new ObjectId());
        List<Post> posts = new ArrayList<Post>();
        posts.add(postMock);
        List<ObjectId> saved_posts = new ArrayList<ObjectId>();
        List<ObjectId> like_posts = new ArrayList<ObjectId>();
        List<ObjectId> like_comments = new ArrayList<ObjectId>();
        user = new User(new ObjectId(), "email", "country", "phoneNumber", "password", "username", "first_name", "last_name", "gender", "dateOfBirth", avatarMock, "last_login", "is_blocked", "time_created", "fullname", followees, follows, postIds, posts, saved_posts, like_posts, like_comments);
    }

    @Test
    public void testGettersAndSetters() {
        ObjectId id = new ObjectId();
        String newEmail = "newEmail";
        String newCountry = "newCountry";
        String newPhoneNumber = "newPhoneNumber";
        String newPassword = "newPassword";
        String newUsername = "newUsername";
        String newFirstName = "newFirstName";
        String newLastName = "newLastName";
        String newGender = "newGender";
        String newDateOfBirth = "newDateOfBirth";
        Media newAvatar = mock(Media.class);
        String newLastLogin = "newLastLogin";
        String newIsBlocked = "newIsBlocked";
        String newTimeCreated = "newTimeCreated";
        String newFullname = "newFullname";
        List<ObjectId> newFollowees = new ArrayList<ObjectId>();
        List<ObjectId> newFollows = new ArrayList<ObjectId>();
        List<ObjectId> newPostIds = new ArrayList<ObjectId>();
        newPostIds.add(new ObjectId());
        List<Post> newPosts = new ArrayList<Post>();
        newPosts.add(mock(Post.class));
        List<ObjectId> newSavedPosts = new ArrayList<ObjectId>();
        List<ObjectId> newLikePosts = new ArrayList<ObjectId>();
        List<ObjectId> newLikeComments = new ArrayList<ObjectId>();

        user.setId(id);
        assertEquals(id, user.getId());

        user.setEmail(newEmail);
        assertEquals(newEmail, user.getEmail());

        user.setCountry(newCountry);
        assertEquals(newCountry, user.getCountry());

        user.setPhoneNumber(newPhoneNumber);
        assertEquals(newPhoneNumber, user.getPhoneNumber());

        user.setPassword(newPassword);
        assertEquals(newPassword, user.getPassword());

        user.setUsername(newUsername);
        assertEquals(newUsername, user.getUsername());

        user.setFirst_name(newFirstName);
        assertEquals(newFirstName, user.getFirst_name());

        user.setLast_name(newLastName);
        assertEquals(newLastName, user.getLast_name());

        user.setGender(newGender);
        assertEquals(newGender, user.getGender());

        user.setDateOfBirth(newDateOfBirth);
        assertEquals(newDateOfBirth, user.getDateOfBirth());

        user.setAvatar(newAvatar);
        assertEquals(newAvatar, user.getAvatar());

        user.setLast_login(newLastLogin);
        assertEquals(newLastLogin, user.getLast_login());

        user.setIs_blocked(newIsBlocked);
        assertEquals(newIsBlocked, user.getIs_blocked());

        user.setTime_created(newTimeCreated);
        assertEquals(newTimeCreated, user.getTime_created());

        user.setFullname(newFullname);
        assertEquals(newFullname, user.getFullname());

        user.setFollowees(newFollowees);
        assertEquals(newFollowees, user.getFollowees());

        user.setFollows(newFollows);
        assertEquals(newFollows, user.getFollows());

        user.setPostIds(newPostIds);
        assertEquals(newPostIds, user.getPostIds());

        user.setPosts(newPosts);
        assertEquals(newPosts, user.getPosts());

        user.setSaved_posts(newSavedPosts);
        assertEquals(newSavedPosts, user.getSaved_posts());

        user.setLike_posts(newLikePosts);
        assertEquals(newLikePosts, user.getLike_posts());

        user.setLike_comments(newLikeComments);
        assertEquals(newLikeComments, user.getLike_comments());
    }

    @Test
    public void testUserConstructor() {
        assertNotNull(user.getId());
        assertEquals("email", user.getEmail());
        assertEquals("country", user.getCountry());
        assertEquals("phoneNumber", user.getPhoneNumber());
        assertEquals("password", user.getPassword());
        assertEquals("username", user.getUsername());
        assertEquals("first_name", user.getFirst_name());
        assertEquals("last_name", user.getLast_name());
        assertEquals("gender", user.getGender());
        assertEquals("dateOfBirth", user.getDateOfBirth());
        assertEquals(avatarMock, user.getAvatar());
        assertEquals("last_login", user.getLast_login());
        assertEquals("is_blocked", user.getIs_blocked());
        assertEquals("time_created", user.getTime_created());
        assertEquals("fullname", user.getFullname());
        assertNotNull(user.getFollowees());
        assertNotNull(user.getFollows());
        assertEquals(1, user.getPostIds().size());
        assertNotNull(user.getPosts());
        assertNotNull(user.getSaved_posts());
        assertNotNull(user.getLike_posts());
        assertNotNull(user.getLike_comments());
    }

    @Test
    public void testGetPostIds() {
        assertEquals(1, user.getPostIds().size());
        assertEquals(ObjectId.class, user.getPostIds().get(0).getClass());
    }

    @Test
    public void testGetFollowees() {
        assertNotNull(user.getFollowees());
        assertEquals(ArrayList.class, user.getFollowees().getClass());
    }

    @Test
    public void testGetFollows() {
        assertNotNull(user.getFollows());
        assertEquals(ArrayList.class, user.getFollows().getClass());
    }

    @Test
    public void testNoArgsConstructor() {
        User user = new User();
        assertNull(user.getId());
        assertNull(user.getEmail());
        assertNull(user.getCountry());
        assertNull(user.getPhoneNumber());
        assertNull(user.getPassword());
        assertNull(user.getUsername());
        assertNull(user.getFirst_name());
        assertNull(user.getLast_name());
        assertNull(user.getGender());
        assertNull(user.getDateOfBirth());
        assertNull(user.getAvatar());
        assertNull(user.getLast_login());
        assertNull(user.getIs_blocked());
        assertNull(user.getTime_created());
        assertNull(user.getFullname());
        assertNotNull(user.getFollowees());
        assertNotNull(user.getFollows());
        assertNotNull(user.getPostIds());
        assertNotNull(user.getPosts());
        assertNotNull(user.getSaved_posts());
        assertNotNull(user.getLike_posts());
        assertNotNull(user.getLike_comments());
    }
}
