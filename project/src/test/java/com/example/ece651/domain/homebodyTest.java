package com.example.ece651.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class homebodyTest {

    @Mock
    private Post postMock;

    private homebody homebody;

    @Before
    public void setUp() {
        homebody = new homebody(postMock, true, false);
    }

    @Test
    public void testGettersAndSetters() {
        Post newPost = new Post();

        homebody.setPost(newPost);
        assertEquals(newPost, homebody.getPost());

        homebody.setWhether_liked(false);
        assertFalse(homebody.getWhether_liked());

        homebody.setWhether_followed_post_user(true);
        assertTrue(homebody.getWhether_followed_post_user());
    }

    @Test
    public void testHomebodyConstructor() {
        assertEquals(postMock, homebody.getPost());
        assertTrue(homebody.getWhether_liked());
        assertFalse(homebody.getWhether_followed_post_user());
    }

    @Test
    public void testEquals() {
        homebody homebody2 = new homebody(postMock, true, false);
        homebody homebody3 = new homebody(new Post(), true, false);

        assertTrue(homebody.equals(homebody2));
        assertFalse(homebody.equals(homebody3));
    }

    @Test
    public void testEqualsWithNull() {
        assertFalse(homebody.equals(null));
    }

    @Test
    public void testEqualsWithDifferentObject() {
        assertFalse(homebody.equals(new Object()));
    }
}