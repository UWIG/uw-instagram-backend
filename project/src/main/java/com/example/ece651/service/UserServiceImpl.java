package com.example.ece651.service;

import com.example.ece651.domain.Comment;
import com.example.ece651.domain.Media;
import com.example.ece651.domain.Searchbody;
import com.example.ece651.domain.User;
import com.mongodb.client.result.UpdateResult;
import jakarta.annotation.Resource;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private MongoTemplate mongoTemplate;

    @Autowired
    private PostService postService;

    @Autowired
    private MediaService mediaService;

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
        Media media = new Media(mediaService.saveFile(avatar));

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
                    .apply(new Update().set("filename", avatar.getOriginalFilename()))
                    .first();
        }
        media.setData(mediaService.downloadFile(avatar.getOriginalFilename()));
        return media;
    }

    @Override
    public User AddUser(User user) {
        User newUser = mongoTemplate.insert(user, COLLECTION_NAME);
        return newUser;
    }

    @Override
    public User FindUserByUsername(String username) {
        Criteria criteria = Criteria.where("username").is(username);
        Query query = new Query(criteria);
        User user = mongoTemplate.findOne(query, User.class, COLLECTION_NAME);
        return user;
    }


    @Override
    public List<User> FindUserByEmail(String email) {
        Criteria criteria = Criteria.where("email").is(email);
        Query query = new Query(criteria);
        List<User> documentList = mongoTemplate.find(query, User.class, COLLECTION_NAME);
        return documentList;
    }

    @Override
    public List<User> FindUserBykeyword(String keyword) {
        Pattern pattern= Pattern.compile("^.*"+keyword+".*$", Pattern.CASE_INSENSITIVE);
        Query query = new Query(Criteria.where("username").regex(pattern));
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
    public String AddUserFollowList(String currentUserName, String targetUserName) {

        User targetUser = FindUserByUsername(targetUserName);
        User currentUser = FindUserByUsername(currentUserName);

        //Add curr_user into targetuser's followee list
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(targetUserName));

        Update update1 = new Update();
        update1.addToSet("followees",currentUser.getId());

        mongoTemplate.updateFirst(query, update1, User.class);

        //Add target_user into curr_user's follow list
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("username").is(currentUserName));

        Update update2 = new Update();
        update2.addToSet("follows",targetUser.getId());

        mongoTemplate.updateFirst(query1, update2, User.class);

        return "successful";
    }

    @Override
    public String DeleteUserFollowList(String currentUserName, String targetUserName) {
        User targetUser = FindUserByUsername(targetUserName);
        User currentUser = FindUserByUsername(currentUserName);

        //Delete curr_user into targetuser's followee list
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(targetUserName));

        Update update1 = new Update();
        update1.pull("followees",currentUser.getId());

        mongoTemplate.updateFirst(query, update1, User.class);

        //Delete target_user into curr_user's follow list
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("username").is(currentUserName));

        Update update2 = new Update();
        update2.pull("follows",targetUser.getId());

        mongoTemplate.updateFirst(query1, update2, User.class);

        return "successful";
    }


    @Override
    public void DeleteUser(User user) {
        Criteria criteria = Criteria.where("email").is(user.getEmail());
        Query query = new Query(criteria);
        List<User> resultList = mongoTemplate.findAllAndRemove(query, User.class, COLLECTION_NAME);
    }

    @Override
    public void savePost(String username, String postId){
        ObjectId id = new ObjectId(postId);
        mongoTemplate.update(User.class).matching(Criteria.where("username").is(username))
                .apply(new Update().push("saved_posts").value(id))
                .first();
    }
    @Override
    public void cancelSavePost(String username, String postId){
        ObjectId id = new ObjectId(postId);
        mongoTemplate.update(User.class).matching(Criteria.where("username").is(username))
                .apply(new Update().pull("saved_posts",id)).first();
    }

    @Override
    public void setFollowing(User user){
        List<Searchbody> followingList = new ArrayList<>();
        if(user.getFollows() != null){
            for(ObjectId id: user.getFollows()){
                User following = FindUserByUserId(id);
                updateUserAvatar(following);
                followingList.add(new Searchbody(following.getAvatar(),true, following.getUsername(),following.getFullname()));
            }
        }
        user.setFollowing(followingList);
    }

    @Override
    public void setFollowers(User user){
        List<Searchbody> followerList = new ArrayList<>();
        if(user.getFollowees() != null){
            for(ObjectId id: user.getFollowees()){
                User follower = FindUserByUserId(id);
                updateUserAvatar(follower);
                boolean isFollowing = false;
                if(user.getFollows() != null){
                    for(ObjectId oid: user.getFollows()){
                        if(id.equals(oid)) {
                            isFollowing = true;
                            break;
                        }
                    }
                }
                followerList.add(new Searchbody(follower.getAvatar(), isFollowing, follower.getUsername(), follower.getFullname()));
            }
        }
        user.setFollowers(followerList);
    }

    @Override
    public Media FindAvatarByUsername(String username) {
        Criteria criteria = Criteria.where("username").is(username);
        Query query = new Query(criteria);
        Media media = mongoTemplate.findOne(query, User.class, "user").getAvatar();
        if(media != null){
            media.setData(mediaService.downloadFile(media.getFilename()));
        }
        return media;
    }

    @Override
    public void updateUserAvatar(User user){
        if(user.getAvatar() != null){
            Media media = user.getAvatar();
            media.setData(mediaService.downloadFile(media.getFilename()));
        }
    }
}
