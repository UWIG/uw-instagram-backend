package com.example.ece651.domain;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.ece651.domain.Comment;
import com.example.ece651.domain.Media;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.mapping.DBRef;

@RunWith(MockitoJUnitRunner.class)
public class CommentTest {

    @Mock
    private Media avatarMock;

    @Mock
    private Comment replyMock;

    private Comment comment;

    @Before
    public void setUp() {
        comment = new Comment("username", avatarMock, "comment");
    }

    @Test
    public void testGettersAndSetters() {
        ObjectId oid = new ObjectId();
        String id = oid.toHexString();
        Date time_created = new Date();
        List<ObjectId> likes = new ArrayList<ObjectId>();
        List<Comment> replies = new ArrayList<Comment>();

        comment.setOid(oid);
        assertEquals(oid, comment.getOid());

        comment.setId(id);
        assertEquals(id, comment.getId());

        comment.setUsername("newUsername");
        assertEquals("newUsername", comment.getUsername());

        Media newAvatar = mock(Media.class);
        comment.setAvatar(newAvatar);
        assertEquals(newAvatar, comment.getAvatar());

        comment.setComment("newComment");
        assertEquals("newComment", comment.getComment());

        comment.setTime_created(time_created);
        assertEquals(time_created, comment.getTime_created());

        likes.add(new ObjectId());
        comment.setLikes(likes);
        assertEquals(likes, comment.getLikes());

        replies.add(replyMock);
        comment.setReplies(replies);
        assertEquals(replies, comment.getReplies());
    }

    @Test
    public void testCommentConstructor() {
        assertNotNull(comment.getOid());
        assertNotNull(comment.getId());
        assertEquals("username", comment.getUsername());
        assertEquals(avatarMock, comment.getAvatar());
        assertEquals("comment", comment.getComment());
        assertNotNull(comment.getTime_created());
        assertEquals(new ArrayList<ObjectId>(), comment.getLikes());
        assertEquals(new ArrayList<Comment>(), comment.getReplies());
    }
}