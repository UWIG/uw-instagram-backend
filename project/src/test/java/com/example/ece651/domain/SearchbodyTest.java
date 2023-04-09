package com.example.ece651.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SearchbodyTest {

    private Searchbody searchbody;

    @Before
    public void setUp() {

    }

    @Test
    public void testGettersAndSetters() {

    }

    @Test
    public void testSearchbodyConstructor() {
        assertNotNull(searchbody.getAvatar());
        assertTrue(searchbody.isFollowing());
        assertEquals("username", searchbody.getUsername());
    }

    @Test
    public void testEquals() {

    }

    @Test
    public void testEqualsWithNull() {
        assertFalse(searchbody.equals(null));
    }

    @Test
    public void testEqualsWithDifferentObject() {
        assertFalse(searchbody.equals(new Object()));
    }
}
