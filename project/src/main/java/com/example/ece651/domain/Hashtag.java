package com.example.ece651.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "hashtag")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hashtag {
    @Id
    private ObjectId id;

    @DBRef
    private List<Post> postList;

    private String tag;

    public Hashtag(String tag, Post post) {
        this.id = new ObjectId();
        this.tag = tag;
        this.postList = new ArrayList<>();
        this.postList.add(post);
    }
}
