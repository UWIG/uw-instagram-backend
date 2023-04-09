package com.example.ece651.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import com.example.ece651.domain.Post;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.mapping.DBRef;

@RunWith(MockitoJUnitRunner.class)
public class HashtagTest {

    @Mock
    private Post postMock;

    private Hashtag hashtag;

    @Before
    public void setUp() {
        hashtag = new Hashtag("tag", postMock);
    }

    @Test
    public void testGettersAndSetters() {
        ObjectId id = new ObjectId();
        List<Post> postList = new ArrayList<Post>();
        Post newPost = mock(Post.class);

        hashtag.setId(id);
        assertEquals(id, hashtag.getId());

        postList.add(newPost);
        hashtag.setPostList(postList);
        assertEquals(postList, hashtag.getPostList());

        hashtag.setTag("newTag");
        assertEquals("newTag", hashtag.getTag());
    }

    @Test
    public void testHashtagConstructor() {
        assertNotNull(hashtag.getId());
        assertEquals("tag", hashtag.getTag());
        assertEquals(1, hashtag.getPostList().size());
        assertEquals(postMock, hashtag.getPostList().get(0));
    }
}