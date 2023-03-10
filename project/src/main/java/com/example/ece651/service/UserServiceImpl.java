package com.example.ece651.service;

import com.example.ece651.domain.Media;
import com.example.ece651.domain.User;
import com.mongodb.client.result.UpdateResult;
import jakarta.annotation.Resource;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private MongoTemplate mongoTemplate;

    private static final String COLLECTION_NAME = "user";

    @Override
    public User UpdateUserByEmail(String id, String email) {
        Criteria criteria = Criteria.where("user_id").is(id);
        Query query = new Query(criteria);
        Update update = new Update().set("email", email);
        UpdateResult result = mongoTemplate.updateFirst(query, update, User.class, COLLECTION_NAME);
        return null;
    }

    @Override
    public User UpdateUserByPhoneNumber(String id, String phoneNumber) {
        Criteria criteria = Criteria.where("user_id").is(id);
        Query query = new Query(criteria);
        Update update = new Update().set("phoneNumber", phoneNumber);
        UpdateResult result = mongoTemplate.updateFirst(query, update, User.class, COLLECTION_NAME);
        return null;
    }

    @Override
    public User UpdateUserByPassword(String id, String password) {
        Criteria criteria = Criteria.where("user_id").is(id);
        Query query = new Query(criteria);
        Update update = new Update().set("password", password);
        UpdateResult result = mongoTemplate.updateFirst(query, update, User.class, COLLECTION_NAME);
        return null;
    }

    @Override
    public User UpdateUserByUsername(String id, String username) {
        Criteria criteria = Criteria.where("user_id").is(id);
        Query query = new Query(criteria);
        Update update = new Update().set("username", username);
        UpdateResult result = mongoTemplate.updateFirst(query, update, User.class, COLLECTION_NAME);
        return null;
    }

    @Override
    public Media UpdateUserByAvatar(String username, MultipartFile avatar) throws IOException {
        Media media = new Media(new Binary(BsonBinarySubType.BINARY, avatar.getBytes()));
//
        Criteria criteria = Criteria.where("username").is(username);
        Query query = new Query(criteria);
        //find the user
        User user = mongoTemplate.find(query, User.class, COLLECTION_NAME).get(0);
//        if the avatar not exist
        if (user.getAvatar() == null) {
            mongoTemplate.insert(media);
            Update update = new Update().set("avatar", media.getId());
            UpdateResult result = mongoTemplate.updateFirst(query, update, User.class, "user");
        }
        //if the avatar exist
        else {
            Media prevMedia = user.getAvatar();
            mongoTemplate.update(Media.class).matching(Criteria.where("_id").is(prevMedia.getId()))
                    .apply(new Update().set("data", media.getData()))
                    .first();
        }
        return media;
    }

    @Override
    public User AddUser(User user) {
        User newUser = mongoTemplate.insert(user, COLLECTION_NAME);
        return newUser;
    }

    @Override
    public List<User> FindUserByUsername(String username) {
        Criteria criteria = Criteria.where("username").is(username);
        Query query = new Query(criteria);
        List<User> documentList = mongoTemplate.find(query, User.class, COLLECTION_NAME);
        return documentList;
    }


    @Override
    public List<User> FindUserByEmail(String email) {
        Criteria criteria = Criteria.where("email").is(email);
        Query query = new Query(criteria);
        List<User> documentList = mongoTemplate.find(query, User.class, COLLECTION_NAME);
        return documentList;
    }

    @Override
    public User FindUserByUserId(ObjectId id) {
        Criteria criteria = Criteria.where("_id").is(id);
        Query query = new Query(criteria);
        List<User> documentList = mongoTemplate.find(query, User.class, COLLECTION_NAME);
        return documentList.get(0);
    }

    @Override
    public Media FindAvatarByUsername(String username) {
        Criteria criteria = Criteria.where("username").is(username);
        Query query = new Query(criteria);
        Media media = mongoTemplate.find(query, User.class, COLLECTION_NAME).get(0).getAvatar();
        return media;
    }

    @Override
    public void DeleteUser(User user) {
        Criteria criteria = Criteria.where("email").is(user.getEmail());
        Query query = new Query(criteria);
        List<User> resultList = mongoTemplate.findAllAndRemove(query, User.class, COLLECTION_NAME);
    }
}
