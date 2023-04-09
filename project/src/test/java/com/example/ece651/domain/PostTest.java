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
public class PostTest {

    @Mock
    private Media avatarMock;

    @Mock
    private Media mediaMock;

    @Mock
    private Comment commentMock;

    private Post post;

    @Before
    public void setUp() {
        List<Media> mediaList = new ArrayList<Media>();
        mediaList.add(mediaMock);
        post = new Post("username", avatarMock, "caption", mediaList);
    }

    @Test
    public void testGettersAndSetters() {
        ObjectId oid = new ObjectId();
        String id = oid.toHexString();
        Media newAvatar = mock(Media.class);
        List<Media> newMediaList = new ArrayList<Media>();
        Media newMedia = mock(Media.class);
        newMediaList.add(newMedia);
        List<ObjectId> likes = new ArrayList<ObjectId>();
        List<Comment> comments = new ArrayList<Comment>();
        Comment newComment = mock(Comment.class);
        comments.add(newComment);
        Date timeCreated = new Date();
        Date timeModified = new Date();

        post.setOid(oid);
        assertEquals(oid, post.getOid());

        post.setId(id);
        assertEquals(id, post.getId());

        post.setUsername("newUsername");
        assertEquals("newUsername", post.getUsername());

        post.setAvatar(newAvatar);
        assertEquals(newAvatar, post.getAvatar());

        post.setType("newType");
        assertEquals("newType", post.getType());

        post.setCaption("newCaption");
        assertEquals("newCaption", post.getCaption());

        post.setLocation("newLocation");
        assertEquals("newLocation", post.getLocation());

        post.setTime_created(timeCreated);
        assertEquals(timeCreated, post.getTime_created());

        post.setTime_modified(timeModified);
        assertEquals(timeModified, post.getTime_modified());

        post.setMediaList(newMediaList);
        assertEquals(newMediaList, post.getMediaList());

        likes.add(new ObjectId());
        post.setLikes(likes);
        assertEquals(likes, post.getLikes());

        post.setComments(comments);
        assertEquals(comments, post.getComments());

        post.setWhether_liked(true);
        assertTrue(post.getWhether_liked());

        post.setWhether_saved(true);
        assertTrue(post.getWhether_saved());

        post.setWhether_followed_post_user(true);
        assertTrue(post.getWhether_followed_post_user());
    }

    @Test
    public void testPostConstructor() {
        assertNotNull(post.getOid());
        assertEquals("username", post.getUsername());
        assertEquals(avatarMock, post.getAvatar());
        assertEquals("caption", post.getCaption());
        assertEquals(new ArrayList<Media>() {{ add(mediaMock); }}, post.getMediaList());
        assertNotNull(post.getTime_created());
        assertNotNull(post.getTime_modified());
        assertFalse(post.getWhether_liked());
        assertFalse(post.getWhether_followed_post_user());
    }
}
