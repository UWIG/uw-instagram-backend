package com.example.ece651.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;

public class MediaTest {

    private Media media;

    @Before
    public void setUp() {
        media = new Media(new Binary("data".getBytes()));
    }

    @Test
    public void testGettersAndSetters() {
        ObjectId id = new ObjectId();
        Binary newData = new Binary("newData".getBytes());

        media.setId(id);
        assertEquals(id, media.getId());

        media.setData(newData);
        assertEquals(newData, media.getData());
    }

    @Test
    public void testMediaConstructor() {
        assertNotNull(media.getId());
        assertEquals(new Binary("data".getBytes()), media.getData());
    }
}
