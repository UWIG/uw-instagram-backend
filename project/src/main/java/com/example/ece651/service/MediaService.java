package com.example.ece651.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.example.ece651.domain.Comment;
import com.example.ece651.domain.Media;
import com.example.ece651.domain.Post;
import com.example.ece651.domain.User;
import jakarta.annotation.Resource;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MediaService {

    @Value("${bucketName}")
    private String bucketName;

    @Resource
    private MongoTemplate mongoTemplate;

    private final AmazonS3 s3;

    public MediaService(AmazonS3 s3){
        this.s3 = s3;
    }

    public List<Media> createMediaList(MultipartFile[] files) throws IOException {
        List<Media> mediaList = new ArrayList<>();
        for(MultipartFile file: files){
            String filetype = file.getContentType().substring(0,5);
            Media media = new Media(saveFile(file), filetype);
            mediaList.add(media);
        }
        return mediaList;
    }


    public String saveFile(MultipartFile file){
        String filename = file.getOriginalFilename();
        try {
            File newFile = convertMultiPartToFile(file);
            s3.putObject(bucketName, filename, newFile);
            return filename;
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Cacheable(value="mediaCache", key = "#filename")
    public byte[] downloadFile(String filename){
        System.out.println("New request with " + filename);
        S3Object s3Object = s3.getObject(bucketName,filename);
        S3ObjectInputStream objectContent = s3Object.getObjectContent();
        try{
            return IOUtils.toByteArray(objectContent);
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }


    @CacheEvict(value="mediaCache", key = "#p0")
    public void deleteFile(Media media){
        ObjectId id = media.getId();
        String filename = media.getFilename();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        mongoTemplate.remove(query,Media.class,"media");
        s3.deleteObject(bucketName,filename);
    }

    public List<String> listAllFiles(){
        ListObjectsV2Result listObjectsV2Result = s3.listObjectsV2(bucketName);
        return listObjectsV2Result.getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException{
        File newFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(newFile);
        fos.write(file.getBytes());
        fos.close();
        return newFile;
    }
}