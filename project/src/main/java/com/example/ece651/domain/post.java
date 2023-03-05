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
public class post {
    @MongoId
    private Object id;

    private String type;
    private String caption;
    private String location;
    private String time_created;
    private String time_modified;

    @DBRef
    private List<Object> likes;

    @DBRef
    private List<Object> comments;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime_created() {
        return time_created;
    }

    public void setTime_created(String time_created) {
        this.time_created = time_created;
    }

    public String getTime_modified() {
        return time_modified;
    }

    public void setTime_modified(String time_modified) {
        this.time_modified = time_modified;
    }

    public List<Object> getLikes() {
        return likes;
    }

    public void setLikes(List<Object> likes) {
        this.likes = likes;
    }

    public List<Object> getComments() {
        return comments;
    }

    public void setComments(List<Object> comments) {
        this.comments = comments;
    }
}
