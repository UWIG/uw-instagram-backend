package com.example.ece651.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
class CreateServiceTest {

    @Autowired
    private CreateService createService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void createCollectionTest() {
        String collectionName = "user1";

        // Check if the collection already exists and delete it if it does
        if (mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.dropCollection(collectionName);
        }

        // Create the collection and check that it exists
        Object result = createService.createCollection();
        Assertions.assertEquals("创建视图成功", result);

        // Clean up by dropping the collection again
        mongoTemplate.dropCollection(collectionName);
    }
}
