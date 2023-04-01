package com.example.ece651.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Data
@ToString
@Builder
@AllArgsConstructor
@Document(collection = "notification")
public class Notification {
    @MongoId
    private ObjectId id;
    private Date time_created;
    private String username_from;
    private String username_to;
    private String type;
    private ObjectId postId;
    private String comment;
    private Boolean whether_read;
    public Notification(String username_from,String username_to,String type,ObjectId postId,String comment){

        this.username_to = username_to;
        this.type = type;
        this.postId = postId;
        this.comment = comment;
        this.id = new ObjectId();
        this.time_created = new Date();
        this.whether_read = false;
        this.username_from = username_from;
    }
    public Notification(){}
}
