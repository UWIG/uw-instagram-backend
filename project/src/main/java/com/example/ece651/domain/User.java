package com.example.ece651.domain;


import lombok.*;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
public class User {
    @MongoId
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
    private String avatar;
    private String last_login;
    private String is_blocked;
    private String time_created;
    private String fullname;
    @DBRef
    private List<ObjectId> followees;
    @DBRef
    private List<ObjectId> posts;
    @DBRef
    private List<ObjectId> saved_posts;

    public List<ObjectId> getFollowees() {
        return followees;
    }

    public void setFollowees(List<ObjectId> followees) {
        this.followees = followees;
    }

    public List<ObjectId> getPosts() {
        return posts;
    }

    public void setPosts(List<ObjectId> posts) {
        this.posts = posts;
    }

    public List<ObjectId> getSaved_posts() {
        return saved_posts;
    }

    public void setSaved_posts(List<ObjectId> saved_posts) {
        this.saved_posts = saved_posts;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public String getIs_blocked() {
        return is_blocked;
    }

    public void setIs_blocked(String is_blocked) {
        this.is_blocked = is_blocked;
    }

    public String getTime_created() {
        return time_created;
    }

    public void setTime_created(String time_created) {
        this.time_created = time_created;
    }

    public User() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId user_id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
