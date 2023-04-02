package com.example.ece651.domain;


import lombok.*;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@Document(collection = "user")
public class User {
    @Id
    private ObjectId id;
    private String email;
    private String country;
    private String phoneNumber;
    private String password;
    private String username;
    private String first_name;
    private String last_name;
    private String gender;
    private String dateOfBirth;
    @DBRef
    private Media avatar;
    private String last_login;
    private String is_blocked;
    private String time_created;
    private String fullname;
    private List<ObjectId> followees;
    private List<ObjectId> follows;
    private List<Searchbody> followers;
    private List<Searchbody> following;
    private List<ObjectId> postIds;
    private List<Post> posts;
    private List<ObjectId> saved_posts;
    private List<ObjectId> like_posts;
    private List<ObjectId> like_comments;
    public User() {
        this.follows = new ArrayList<ObjectId>() ;
        this.followees = new ArrayList<ObjectId>() ;
        this.like_posts = new ArrayList<ObjectId>() ;
        this.saved_posts = new ArrayList<ObjectId>() ;
    }
}
