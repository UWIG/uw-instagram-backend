package com.example.ece651.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "media")
@Data
public class Media {
    @Id
    private ObjectId id;

    private Binary data;

    public Media(Binary data) {
        this.id = new ObjectId();
        this.data = data;
    }
}
