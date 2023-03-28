package com.example.ece651.service;

import com.example.ece651.domain.Hashtag;
import com.example.ece651.domain.Post;
import com.example.ece651.domain.User;
import jakarta.annotation.Resource;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class HashtagService {

    @Resource
    private MongoTemplate mongoTemplate;

    public void addHashtags(String hashtags, Post post) throws IOException {
        String[] tags = hashtags.split(",");
        for(String tag: tags){
            //if tag in the database
            String hashtag = tag.substring(1);
            if(getHashtag(hashtag) != null){
                System.out.println("exists");
                mongoTemplate.update(Hashtag.class).matching(Criteria.where("tag").is(hashtag))
                        .apply(new Update().push("postList").value(post))
                        .first();
            }else{
            //tag not in the database
                Hashtag newHashtag = new Hashtag(tag, post);
                mongoTemplate.insert(newHashtag,"hashtag");
            }
        }
    }

    public Hashtag getHashtag(String hashtag) {
        Criteria criteria = Criteria.where("tag").is(hashtag);
        Query query = new Query(criteria);
        return mongoTemplate.findOne(query, Hashtag.class, "hashtag");
    }


    public List<Post> getPostsByHashtag(String hashtag){
        Hashtag tag = getHashtag(hashtag);
        System.out.println(tag.getTag());
        return tag.getPostList();
    }
}