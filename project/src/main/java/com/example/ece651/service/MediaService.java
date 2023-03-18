package com.example.ece651.service;

import com.example.ece651.domain.Media;
import com.example.ece651.domain.User;
import jakarta.annotation.Resource;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class MediaService {

    @Resource
    private MongoTemplate mongoTemplate;

    public List<Media> createMediaList(MultipartFile[] files) throws IOException {
        List<Media> mediaList = new ArrayList<>();
        for(MultipartFile file: files){
            Media media = new Media(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
            mediaList.add(media);
        }
        return mediaList;
    }
//    public String getMedia(ObjectId id) {
//        Criteria criteria = Criteria.where("_id").is(id);
//        Query query = new Query(criteria);
//        //find the user
//        Media media = mongoTemplate.findOne(query, Media.class, "media");
//        return Base64.getEncoder().encodeToString(image.getData().getData());
//    }
//
}