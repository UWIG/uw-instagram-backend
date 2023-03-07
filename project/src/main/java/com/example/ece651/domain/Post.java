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

    private String type;
    private String caption;
    private String location;
    private Date time_created;
    private Date time_modified;

    @DBRef
    private List<Media> mediaList;

    @DBRef
    private List<Object> likes;

    @DBRef
    private List<Comment> comments;

    public Post() {
    }

    public Post(String caption, List<Media> mediaList) {
        this.caption = caption;
        this.mediaList = mediaList;
        this.oid = new ObjectId();
        this.id = this.oid.toHexString();
        this.time_created = new Date();
        this.time_modified = new Date();
    }


}
