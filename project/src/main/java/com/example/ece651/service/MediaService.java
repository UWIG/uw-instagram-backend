package com.example.ece651.service;

import com.example.ece651.domain.Media;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class MediaService {

    public List<Media> createMediaList(MultipartFile[] files) throws IOException {
        List<Media> mediaList = new ArrayList<>();
        for(MultipartFile file: files){
            Media media = new Media(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
            mediaList.add(media);
        }
        return mediaList;
    }

//    public String getMedia(String id) {
////        Media image = mediaRepo.findById(id).get();
//        return Base64.getEncoder().encodeToString(image.getData().getData());
//    }
//
}