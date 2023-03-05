package com.example.ece651.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
public class Comment {

    @MongoId
    private Object id;
    private String comment;
    private String time_created;
    @DBRef
    private List<Object> likes;
    @DBRef
    private List<String> replies;

    public Comment(){

    }


    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime_created() {
        return time_created;
    }

    public void setTime_created(String time_created) {
        this.time_created = time_created;
    }

    public List<Object> getLikes() {
        return likes;
    }

    public void setLikes(List<Object> likes) {
        this.likes = likes;
    }

    public List<String> getHashtags() {
        return replies;
    }

    public void setHashtags(List<String> replies) {
        this.replies = replies;
    }
}
