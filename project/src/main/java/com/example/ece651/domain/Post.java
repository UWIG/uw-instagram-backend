package com.example.ece651.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@Document(collection = "post")
public class Post {
    @Id
    private ObjectId oid;

    private String id;
    private String username;
    @DBRef
    private Media avatar;

    private String type;
    private String caption;
    private String location;
    private Date time_created;
    private Date time_modified;

    @DBRef
    private List<Media> mediaList;


    private List<ObjectId> likes;

    @DBRef
    private List<Comment> comments;

    private Boolean whether_liked;
    private Boolean whether_followed_post_user;

    public Post() {
    }

    public Post(String username, Media avatar, String caption, List<Media> mediaList) {
        this.username = username;
        this.avatar = avatar;
        this.caption = caption;
        this.mediaList = mediaList;
        this.oid = new ObjectId();
        this.id = this.oid.toHexString();
        this.time_created = new Date();
        this.time_modified = new Date();
        this.whether_liked = false;
        this.whether_followed_post_user = false;
    }


}
