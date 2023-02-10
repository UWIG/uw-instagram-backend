package com.example.ece651.domain;


import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@ToString
@Builder
@AllArgsConstructor
public class User {
    @MongoId
    private String id;
    private String email;
    private String phoneNumber;
    private String password;
    private String username;

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String user_id) {
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
