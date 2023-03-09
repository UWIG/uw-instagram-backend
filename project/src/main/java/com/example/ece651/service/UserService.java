package com.example.ece651.service;

import com.example.ece651.domain.User;
import org.bson.types.ObjectId;

import java.util.List;

public interface UserService {
    User UpdateUserByEmail(String id, String Email);
    User UpdateUserByPhoneNumber(String id, String PhoneNumber);
    User UpdateUserByPassword(String id, String Password);
    User UpdateUserByUsername(String id, String Username);
    User AddUser(User user);
    List<User> FindUserByUsername(String username);
    List<User> FindUserByEmail(String email);
    User FindUserByUserId(ObjectId id);
    void DeleteUser(User user);
}
