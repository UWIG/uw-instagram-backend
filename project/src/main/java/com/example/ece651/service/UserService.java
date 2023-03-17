package com.example.ece651.service;

import com.example.ece651.domain.Media;
import com.example.ece651.domain.User;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    User UpdateUserByEmail(String id, String Email);
    User UpdateUserByPhoneNumber(String id, String PhoneNumber);
    User UpdateUserByPassword(String id, String Password);
    User UpdateUserByUsername(String id, String Username);
    Media UpdateUserByAvatar(String username, MultipartFile avatar) throws IOException;
    User AddUser(User user);
    List<User> FindUserByUsername(String username);
    List<User> FindUserByEmail(String email);
    List<User> FindUserBykeyword(String keyword);
    User FindUserByUserId(ObjectId id);
    Media FindAvatarByUsername(String username);
    String AddUserFollowList(String currentUserName, String targetUserName);
    String DeleteUserFollowList(String currentUserName, String targetUserName);
    void DeleteUser(User user);

}
