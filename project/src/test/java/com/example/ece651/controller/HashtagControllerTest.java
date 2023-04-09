package com.example.ece651.controller;

import com.example.ece651.domain.Post;
import com.example.ece651.service.HashtagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class HashtagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HashtagService hashtagService;

    @Test
    void getPosts() throws Exception {
        String hashtag = "test";
        Post post1 = new Post();
        post1.setId("1");
        post1.setCaption("This is a test post #test");

        Post post2 = new Post();
        post2.setId("2");
        post2.setCaption("Another test post #test");

        List<Post> posts = Arrays.asList(post1, post2);
        when(hashtagService.getPostsByHashtag(hashtag)).thenReturn(posts);

        mockMvc.perform(get("/hashtags/{hashtag}", hashtag))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[0].caption").value("This is a test post #test"))
                .andExpect(jsonPath("$.[1].id").value("2"))
                .andExpect(jsonPath("$.[1].caption").value("Another test post #test"));

        verify(hashtagService, times(1)).getPostsByHashtag(hashtag);
    }
}