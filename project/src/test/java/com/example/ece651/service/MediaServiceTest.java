package com.example.ece651.service;

import com.example.ece651.domain.Media;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MediaServiceTest {
    @Autowired
    MediaService mediaService;

    @Resource
    private MongoTemplate mongoTemplate;
    @Test
    void createMediaList() throws IOException {
        byte[] fileData1 = "Test file data 1".getBytes();
        byte[] fileData2 = "Test file data 2".getBytes();

        MultipartFile[] files = new MultipartFile[2];
        files[0] = new MockMultipartFile("file1", "file1.jpg", "image/jpeg", fileData1);
        files[1] = new MockMultipartFile("file2", "file2.jpg", "image/jpeg", fileData2);

        List<Media> mediaList = mediaService.createMediaList(files);

        Assertions.assertEquals(2, mediaList.size());
        Assertions.assertArrayEquals(fileData1, mediaList.get(0).getData().getData());
        Assertions.assertArrayEquals(fileData2, mediaList.get(1).getData().getData());
    }
}